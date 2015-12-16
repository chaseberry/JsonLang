package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

class AddFunction : NativeFunction("+", Type.Number) {

    override val params: List<ParameterDefinition> = listOf(ParameterDefinition("i", Type.Number),
            ParameterDefinition("j", Type.Number))

    override fun execute(parent:String, vararg params: Value): Any? {
        return (params[0].value as Number).toDouble() + (params[1].value as Number).toDouble()
    }


}