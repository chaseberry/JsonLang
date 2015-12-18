package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type

class SetFunction(e: Engine) : NativeFunction(e, "set", null) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("name", Type.String),
            ParameterDefinition("value", Type.MAny))

    override fun execute(parent: String, params: ParamMap): Any? {
        val key = "$parent.${params.getString("name")}"
        engine.mem[key] = params.getValue("value")
        return null
    }
}