package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.exceptions.JLParseException
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value
import edu.csh.chase.kjson.JsonArray
import edu.csh.chase.kjson.JsonObject

class JsonLangValueParser(val obj: Any?, val parent: String) {

    val value: Value by lazy {
        parse()
    }

    private fun parse(): Value {
        return when (obj) {
            is Number -> Value(obj, Type(RawType.Number))
            is String -> Value(obj, Type(RawType.String))
            is Boolean -> Value(obj, Type(RawType.Boolean))
            is JsonObject -> parseObject(obj)
            is JsonArray -> Value(obj.map { JsonLangValueParser(it, parent).value }, Type(RawType.Array))
            else -> Value(null, Type(RawType.MAny))
        }
    }

    private fun parseObject(o: JsonObject): Value {
        return try {
            Value(JsonLangActionParser(o, parent).action, Type(RawType.Action))
        } catch(e: JLParseException) {
            val map = o.associateBy({ it.key }) {
                JsonLangValueParser(it.value, parent).value
            }
            Value(map, Type(RawType.Object))
        }
    }

}