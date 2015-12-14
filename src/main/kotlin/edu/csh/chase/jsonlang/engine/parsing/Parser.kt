package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.models.Parameter
import edu.csh.chase.jsonlang.engine.models.Program
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value
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

    fun parseParameter(obj: JsonObject): Parameter? {
        val name = obj.getString("name") ?: return null
        val value = obj["value"]
    }

    fun parseValue(value: Any?): Value? {
        return when (value) {
            is Number -> Value(value, Type.Number)
            is String -> Value(value, Type.String)
            is Boolean -> Value(value, Type.Boolean)
            is JsonObject -> Value(value, Type.Object)
            is JsonArray -> Value(value, Type.Array)
            null -> Value(value, Type.Any)
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