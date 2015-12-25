package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Value

class SetFunction(e: Engine) : NativeFunction(e, "set", null) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("name", RawType.String),
            ParameterDefinition("value", RawType.MAny))

    override fun execute(parent: String, params: ParamMap): Value? {
        val key = "$parent.${params.getString("name")}"
        engine.mem[key] = params.getValue("value")
        return null
    }
}