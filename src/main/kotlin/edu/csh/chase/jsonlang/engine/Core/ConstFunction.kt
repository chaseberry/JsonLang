package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value
import edu.csh.chase.jsonlang.engine.pd

class ConstFunction(e: Engine) : NativeFunction(e, "const", Type.MAny) {

    override val parameters: List<ParameterDefinition> = listOf(pd("value", Type.MAny))

    override fun execute(parent: String, params: ParamMap): Value? {
        return params.getValue("value")
    }
}