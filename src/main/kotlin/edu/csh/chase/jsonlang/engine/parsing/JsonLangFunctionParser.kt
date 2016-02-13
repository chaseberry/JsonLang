package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.exceptions.JLParseException
import edu.csh.chase.jsonlang.engine.models.Action
import edu.csh.chase.jsonlang.engine.models.Function
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.parseError
import edu.csh.chase.kjson.JsonObject
import java.util.*

class JsonLangFunctionParser(val obj: JsonObject, val parent: String) {

    constructor(str: String, parent: String) : this(JsonObject(str), parent)

    val function: Function by lazy {
        parse()
    }

    private fun parse(): Function {
        val name = obj.getString("name") ?: throw parseError("No 'name' given to a function", parent)
        val arr = obj.getJsonArray("parameters")
        val params = arr?.map {
            if (it !is JsonObject) {
                throw JLParseException("A 'parameter' object must be an object in function", "$parent.$name")
            }
            JsonLangParameterDefinitionParser(it, "$parent.$name").parameterDefinition
        }?.toCollection(arrayListOf<ParameterDefinition>())
        val actionsArr = obj.getJsonArray("actions") ?: throw parseError("No 'actions' array in function", "$parent.$name")
        val actions = actionsArr.map {
            if (it !is JsonObject) {
                throw parseError("A 'action' object must be an object in function", "$parent.$name")
            }
            JsonLangActionParser(it, "$parent.$name").action
        }.toCollection(arrayListOf<Action>())
        val returns = obj.getString("returns")
        val returnType = if (returns != null) JsonLangTypeParser(returns, parent) else null
        return Function(name, params ?: ArrayList(), actions, returnType?.type)
    }


}