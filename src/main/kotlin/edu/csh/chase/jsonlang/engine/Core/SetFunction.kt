package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.*
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Value

class SetFunction(e: Engine) : NativeFunction(e, "set", null) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("name", stringType()),
            ParameterDefinition("value", mAnyType()))

    override fun execute(parent: String, params: ParamMap): Value? {
        val key = params.getString("name")
        engine.setFrameMemoryValue(key, params.getValue("value"))
        return null
    }
}