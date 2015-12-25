package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.exceptions.ParseException
import edu.csh.chase.jsonlang.engine.models.Action
import edu.csh.chase.jsonlang.engine.models.Value
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
        val name = obj.getString("name") ?: throw ParseException("No 'name' given to an action in $parent")
        val arr = obj.getJsonArray("parameters") ?: throw ParseException("No 'parameters' array in action $parent.$name")
        val params = arr.map {
            if ( it !is JsonObject) {
                throw ParseException("A 'parameter' must be an object in action $parent.$name")
            }
            Parser.parseParameter(it, "$parent.$name")
        }.toArrayList().toMap({ it.name }, { it.value })
        return Action(name, params)
    }

    private fun parseShortAction(obj: JsonObject, parent: String): Action {
        val name = obj.keySet.first()

        val params = obj[name] as? JsonObject ?: throw ParseException("A shorthand action requires an object for parameters. " +
                "At $parent.$name")
        val paramList = HashMap<String, Value>()

        params.forEach {
            paramList.put(it.key, Parser.parseValue(it.value))
        }

        return Action(name, paramList)
    }

}