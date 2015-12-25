package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.*
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Value

class GlobalSetFunction(e: Engine) : NativeFunction(e, "setGlobal", null) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("name", stringType()),
            ParameterDefinition("value", mAnyType()))

    override fun execute(parent: String, params: ParamMap): Value? {
        engine.mem[params.getString("name")] = params.getValue("value")
        return null
    }


}