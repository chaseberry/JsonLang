package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.exceptions.ParseException
import edu.csh.chase.jsonlang.engine.models.*
import edu.csh.chase.jsonlang.engine.models.Function
import edu.csh.chase.kjson.JsonArray
import edu.csh.chase.kjson.JsonObject
import java.util.*

object Parser {

    fun parseProgram(str: String): Program? {
        return parseProgram(JsonObject(str))
    }

    fun parseProgram(obj: JsonObject): Program {
        val name = obj.getString("name") ?: throw ParseException("No 'name' given to Program")
        val arr = obj.getJsonArray("functions") ?: throw ParseException("No 'functions' array in program $$name")
        val functions = arr.map {
            if (it !is JsonObject) {
                throw ParseException("A 'functions' object must be an object in program $$name")
            }
            parseFunction(it, "$name")
        }.toArrayList()
        return Program(name, functions)
    }

    fun parseFunction(obj: JsonObject, parent: String): Function {
        val name = obj.getString("name") ?: throw ParseException("No 'name' given to a function in $parent")
        val arr = obj.getJsonArray("parameters")
        val params = arr?.map {
            if (it !is JsonObject) {
                throw ParseException("A 'parameters' object must be an object in function $parent.$name")
            }
            parseParameterDefinition(it, "$parent.$name")
        }?.toArrayList()
        val actionsArr = obj.getJsonArray("actions") ?: throw ParseException("No 'actions' array in function $parent.$name")
        val actions = actionsArr.map {
            if (it !is JsonObject) {
                throw ParseException("A 'actions' object bust be an object in function $parent.$name")
            }
            parseAction(it, "$parent.$name")
        }.toArrayList()
        val returns = obj.getString("returns")
        return Function(name, params ?: ArrayList(), actions, if (returns != null) parseType(returns, parent) else null)
    }

    fun parseParameterDefinition(obj: JsonObject, parent: String): ParameterDefinition {
        val name = obj.getString("name") ?: throw ParseException("No 'name' given to a 'parameters' object in $parent")
        val type = obj.getString("type") ?: throw ParseException("NO 'type' give to a 'parameters' object $parent.$name")
        return ParameterDefinition(name, parseType(type, parent))
    }

    fun parseAction(obj: JsonObject, parent: String): Action {
        val name = obj.getString("name") ?: throw ParseException("No 'name' given to an action in $parent")
        val arr = obj.getJsonArray("parameters") ?: throw ParseException("No 'parameters' array in action $parent.$name")
        val params = arr.map {
            if ( it !is JsonObject) {
                throw ParseException("A 'parameter' must be an object in action $parent.$name")
            }
            parseParameter(it, "$parent.$name")
        }.toArrayList().toMap({ it.name }, { it.value })
        return Action(name, params)
    }

    fun parseActionShorthand(obj: JsonObject, parent: String): Action {
        if (obj.keySet.size != 1) {
            throw ParseException("A shorthand action can only contain 1 top level key. Got ${obj.keySet.size}. At" +
                    " $parent")
        }

        val name = obj.keySet.first()

        val params = obj[name] as? JsonObject ?: throw ParseException("A shorthand action requires an object for " +
                "parameters. At $parent.$name")
        val paramList = HashMap<String, Value>()

        params.forEach {
            paramList.put(it.key, parseValue(it.value))
        }

        return Action(name, paramList)
    }

    fun parseParameter(obj: JsonObject, parent: String): Parameter {
        val name = obj.getString("name") ?: throw ParseException("No 'name' in a parameter in action $parent")
        val value = parseValue(obj["value"])
        return Parameter(name, value)
    }

    fun parseValue(value: Any?): Value {
        return when (value) {
            is Number -> Value(value, Type(RawType.Number))
            is String -> Value(value, Type(RawType.String))
            is Boolean -> Value(value, Type(RawType.Boolean))
            is JsonObject -> {
                val action = unsafeParseAction(value)
                if (action == null) {
                    Value(value, Type(RawType.Object))//TODO needs to be passed into a Map<String, Value>
                } else {
                    Value(action, Type(RawType.Action))
                }
            }
            is JsonArray -> Value(value.map { parseValue(it) }, Type(RawType.Array))
            null -> Value(value, Type(RawType.MAny))
            else -> Value(null, Type(RawType.MAny))
        }
    }

    fun parseType(str: String, parent: String): Type {
        if (str.contains(":")) {
            return parseGenericType(str, parent)
        }
        return when (str.toLowerCase()) {
            "number" -> Type(RawType.Number)
            "?number" -> Type(RawType.MNumber)
            "boolean" -> Type(RawType.Boolean)
            "?boolean" -> Type(RawType.MBoolean)
            "string" -> Type(RawType.String)
            "?String" -> Type(RawType.String)
            "object" -> Type(RawType.Object)
            "?object" -> Type(RawType.MObject)
            "array" -> Type(RawType.Array)
            "?array" -> Type(RawType.MArray)
            "any" -> Type(RawType.Any)
            "action" -> Type(RawType.Action)
            else -> throw ParseException("'str' is not a Type. At $parent")
        }
    }

    fun parseGenericType(str: String, parent: String): Type {
        val parts = str.split(":")
        if (parts.size != 2) {
            return Type(RawType.MAny)
        }

        val gt = parseType(parts[1], parent)
        if (gt is GenericType) {
            throw ParseException("The sub-type of a Generic Type cannot be a Generic Type. At $parent")
        }

        return when (parts[0].toLowerCase()) {
            "object" -> GenericType(RawType.Object, gt.type)
            "array" -> GenericType(RawType.Array, gt.type)
            "?object" -> GenericType(RawType.MObject, gt.type)
            "?array" -> GenericType(RawType.MArray, gt.type)
            else -> throw ParseException("The main type of a Generic Type must be either ?Array, ?Object, Array, or Object. " +
                    "At $parent")
        }
    }

    fun unsafeParseAction(obj: JsonObject): Action? {
        val name = obj.getString("name") ?: return null
        val arr = obj.getJsonArray("parameters") ?: return null
        val params = arr.map {
            if ( it !is JsonObject) {
                return null
            }
            unsafeParseParameter(it) ?: return null
        }.toArrayList().toMap({ it.name }, { it.value })
        return Action(name, params)
    }

    fun unsafeParseParameter(obj: JsonObject): Parameter? {
        val name = obj.getString("name") ?: return null
        val value = parseValue(obj["value"])
        return Parameter(name, value)
    }

}