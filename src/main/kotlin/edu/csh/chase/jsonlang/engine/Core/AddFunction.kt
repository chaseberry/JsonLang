package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type

class AddFunction : NativeFunction("+", Type.Number) {

    override val params: List<ParameterDefinition> = listOf(ParameterDefinition("i", Type.Number),
            ParameterDefinition("j", Type.Number))

    override fun execute(vararg params: Any?): Any? {
        return (params[0] as Number).toDouble() + (params[1] as Number).toDouble()
    }


}