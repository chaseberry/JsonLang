package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type

class PrintFunction : NativeFunction(null) {

    override val params: List<ParameterDefinition> = listOf(ParameterDefinition("message", Type.MAny))

    override fun execute(vararg params: Any?) {
        print(params[0])
    }


}