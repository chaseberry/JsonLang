package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

class PrintFunction : NativeFunction("print", null) {

    override val params: List<ParameterDefinition> = listOf(ParameterDefinition("message", Type.MAny))

    override fun execute(vararg params: Value): Any? {
        print(params[0].value)
        return null
    }


}