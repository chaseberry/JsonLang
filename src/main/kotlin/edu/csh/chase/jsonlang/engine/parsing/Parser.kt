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
        return Function(name, params ?: ArrayList(), actions, if (returns != null) parseType(returns) else null)
    }

    fun parseParameterDefinition(obj: JsonObject, parent: String): ParameterDefinition {
        val name = obj.getString("name") ?: throw ParseException("No 'name' given to a 'parameters' object in $parent")
        val type = obj.getString("type") ?: throw ParseException("NO 'type' give to a 'parameters' object $parent.$name")
        return ParameterDefinition(name, parseType(type))
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

    fun parseParameter(obj: JsonObject, parent: String): Parameter {
        val name = obj.getString("name") ?: throw ParseException("No 'name' in a parameter in action $parent")
        val value = parseValue(obj["value"])
        return Parameter(name, value)
    }

    fun parseValue(value: Any?): Value {
        return when (value) {
            is Number -> Value(value, Type.Number)
            is String -> Value(value, Type.String)
            is Boolean -> Value(value, Type.Boolean)
            is JsonObject -> {
                val action = unsafeParseAction(value)
                if (action == null) {
                    Value(value, Type.Object)//TODO needs to be pased into a Map<String, Value>
                } else {
                    Value(action, Type.Action)
                }
            }
            is JsonArray -> Value(value.map { parseValue(it) }, Type.Array)
            null -> Value(value, Type.MAny)
            else -> Value(null, Type.MAny)
        }
    }

    fun parseType(str: String): Type {
        return when (str.toLowerCase()) {
            "number" -> Type.Number
            "?number" -> Type.MNumber
            "boolean" -> Type.Boolean
            "?boolean" -> Type.MBoolean
            "string" -> Type.String
            "?String" -> Type.String
            "object" -> Type.Object
            "?object" -> Type.MObject
            "array" -> Type.Array
            "?array" -> Type.MArray
            "any" -> Type.Any
            "action" -> Type.Action
            else -> Type.MAny
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