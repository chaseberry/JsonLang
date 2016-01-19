package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.*
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Value

class GlobalGetFunction(e: Engine) : NativeFunction(e, "getGlobal", mAnyType()) {
    override val parameters: List<ParameterDefinition>
        get() = listOf(ParameterDefinition("name", stringType()))

    override fun execute(parent: String, params: ParamMap): Value? {
        val name = params.getString("name")
        return engine.getGlobalMemoryValue(name)
    }
}