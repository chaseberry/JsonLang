package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.*
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Value

class IfFunction(e: Engine) : NativeFunction(e, "if", mAnyType()) {
    override val parameters: List<ParameterDefinition> = listOf(pd("condition", booleanType()),
            pd("then", anyType()), pd("else", mAnyType()))

    override fun execute(parent: String, params: ParamMap): Value? {
        return if (params.getBoolean("condition")) params.getValue("then") else params.getValue("else")

    }
}