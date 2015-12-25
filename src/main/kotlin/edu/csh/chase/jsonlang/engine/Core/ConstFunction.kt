package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.*
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Value

class ConstFunction(e: Engine) : NativeFunction(e, "const", mAnyType()) {

    override val parameters: List<ParameterDefinition> = listOf(pd("value", mAnyType()))

    override fun execute(parent: String, params: ParamMap): Value? {
        return params.getValue("value")
    }
}