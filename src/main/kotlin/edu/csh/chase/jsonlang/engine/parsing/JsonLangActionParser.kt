package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.exceptions.JLParseException
import edu.csh.chase.jsonlang.engine.models.Action
import edu.csh.chase.jsonlang.engine.models.Value
import edu.csh.chase.jsonlang.engine.parseError
import edu.csh.chase.kjson.JsonObject
import java.util.*

class JsonLangActionParser(val obj: JsonObject, val parent: String) {

    val action: Action by lazy {
        parse()
    }

    private fun parse(): Action {
        if (obj.keySet.size == 1) {
            return parseShortAction(obj, parent)
        }
        return parseNormalAction(obj, parent)
    }

    private fun parseNormalAction(obj: JsonObject, parent: String): Action {
        val name = obj.getString("name") ?: throw parseError("No 'name' given to action", parent)
        val arr = obj.getJsonArray("parameters") ?: throw parseError("No 'parameters' array in action", "$parent.$name")
        val params = arr.toMap({
            if ( it !is JsonObject) {
                throw JLParseException("A 'parameter' must be an object in action", "$parent.$name")
            }
            it.getString("name") ?: throw parseError("A 'Parameter' must have a name in", "$parent.$name")
        }) {
            if ( it !is JsonObject) {
                throw parseError("A 'parameter' must be an object in action", "$parent.$name")
            }
            val pVal = it["value"]
            JsonLangValueParser(pVal, parent).value
        }
        return Action(name, params)
    }

    private fun parseShortAction(obj: JsonObject, parent: String): Action {
        val name = obj.keySet.first()

        val params = obj[name] as? JsonObject ?: throw parseError("A shorthand action requires an object for parameters",
                "At $parent.$name")
        val paramList = HashMap<String, Value>()

        params.forEach {
            paramList.put(it.key, JsonLangValueParser(it.value, parent).value)
        }

        return Action(name, paramList)
    }

}