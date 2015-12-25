package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.mAnyType
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Value

class PrintFunction(e: Engine) : NativeFunction(e, "print", null) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("message", mAnyType()))

    override fun execute(parent: String, params: ParamMap): Value? {
        print(params.getAnyM("message"))
        return null
    }


}