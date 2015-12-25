package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.exceptions.JLParseException
import edu.csh.chase.jsonlang.engine.models.ActionType
import edu.csh.chase.jsonlang.engine.models.GenericType
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.parseError
import edu.csh.chase.kjson.JsonObject

class JsonLangTypeParser(private val obj: Any, val parent: String) {

    val type: Type by lazy { parse() }

    private fun parse(): Type {
        if (obj is JsonObject) {
            //Action with specific type
            return parseActionType(obj)
        }
        if (obj is String) {
            //Just a type
            return parseStringType(obj)
        }
        throw parseError("Error parsing '$obj'", parent)
    }

    private fun parseStringType(str: String): Type {
        if (":" in str) {
            return parseGenericType(str)
        }
        return Type(parseTypeFromString(str))
    }

    private fun parseGenericType(str: String): Type {
        val parts = str.split(":")
        return GenericType(parseTypeFromString(parts[0]), parseTypeFromString(parts[1]))
    }

    private fun parseActionType(obj: JsonObject): Type {
        val parameters = obj.getJsonArray("parameters") ?: throw parseError("No 'parameters' array given to action", parent)
        val returns = obj.getString("returns")
        return ActionType(parameters.map {
            if (it !is String) {
                throw parseError("A value in the parameters array is not a String", parent)
            }
            parseTypeFromString(it)
        }.toList(), if (returns != null) parseTypeFromString(returns) else null)
    }

    private fun parseTypeFromString(str: String): RawType {
        return when (str.toLowerCase()) {
            "number" -> RawType.Number
            "?number" -> RawType.MNumber
            "boolean" -> RawType.Boolean
            "?boolean" -> RawType.MBoolean
            "string" -> RawType.String
            "?String" -> RawType.String
            "object" -> RawType.Object
            "?object" -> RawType.MObject
            "array" -> RawType.Array
            "?array" -> RawType.MArray
            "any" -> RawType.Any
            "action" -> RawType.Action
            else -> throw parseError("'$str' is not a valid Type.", parent)
        }
    }
}