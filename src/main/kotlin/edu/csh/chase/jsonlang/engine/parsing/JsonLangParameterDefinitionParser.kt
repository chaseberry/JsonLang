package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.parseError
import edu.csh.chase.kjson.JsonObject

class JsonLangParameterDefinitionParser(val obj: JsonObject, val parent: String) {

    val parameterDefinition: ParameterDefinition by lazy {
        parse()
    }

    private fun parse(): ParameterDefinition {
        return when (obj.size) {
            1 -> parseParameterDefinitionShorthand(obj, parent)
            else -> parseParameterDefinition(obj, parent)
        }
    }

    private fun parseParameterDefinitionShorthand(obj: JsonObject, parent: String): ParameterDefinition {
        val name = obj.keySet.firstOrNull() ?: throw parseError("No name given to a 'parameter' object", parent)
        val type = obj[name] ?: throw parseError("A type must be provided for a 'parameter' object", parent)
        return ParameterDefinition(name, JsonLangTypeParser(type, parent).type)
    }

    private fun parseParameterDefinition(obj: JsonObject, parent: String): ParameterDefinition {
        val name = obj.getString("name") ?: throw parseError("No 'name' given to a 'parameter' object", parent)
        val type = obj.getString("type") ?: throw parseError("NO 'type' give to a 'parameter' object", "$parent.$name")
        return ParameterDefinition(name, JsonLangTypeParser(type, parent).type)
    }

}