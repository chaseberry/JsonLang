package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.models.*
import edu.csh.chase.jsonlang.engine.models.Function
import edu.csh.chase.kjson.JsonArray
import edu.csh.chase.kjson.JsonObject

object Parser {

    fun parseProgram(str: String): Program? {
        return parseProgram(JsonObject(str))
    }

    fun parseProgram(obj: JsonObject): Program? {
        val module = obj.getString("module") ?: return null
        val name = obj.getString("name") ?: return null

    }

    fun parseFunction(obj: JsonObject): Function {
        val name = obj.getString("name")
    }

    fun parseAction(obj: JsonObject): Action? {
        val name = obj.getString("name") ?: return null
        val arr = obj.traverseMulti("p", "params", "parameters") as? JsonArray ?: return null
        val params = arr.map {
            if ( it !is JsonObject) {
                return null
            }
            parseParameter(it) ?: return null
        }.toArrayList()
        return Action(name, params)
    }

    fun parseParameter(obj: JsonObject): Parameter? {
        val name = obj.getString("name") ?: return null
        val value = parseValue(obj["value"]) ?: return null
        return Parameter(name, value)
    }

    fun parseValue(value: Any?): Value? {
        return when (value) {
            is Number -> Value(value, Type.Number)
            is String -> Value(value, Type.String)
            is Boolean -> Value(value, Type.Boolean)
            is JsonObject -> Value(value, Type.Object)
            is JsonArray -> Value(value, Type.Array)
            null -> Value(value, Type.MAny)
            else -> null
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
            else -> Type.MAny
        }
    }

}