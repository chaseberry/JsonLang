package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

class PrintFunction(e: Engine) : NativeFunction(e, "print", null) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("message", Type.MAny))

    override fun execute(parent: String, params: Map<String, Value>): Any? {
        print(engine.getValue(parent, params["message"]!!))
        return null
    }


}