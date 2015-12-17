package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

class SetFunction(e: Engine) : NativeFunction(e, "set", null) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("name", Type.String),
            ParameterDefinition("value", Type.MAny))

    override fun execute(parent: String, params: Map<String, Value>): Any? {
        engine.mem["$parent.${engine.getValue(parent, params["name"]!!).value as String}"] = params["value"]!!
        return null
    }
}