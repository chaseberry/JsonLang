package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

class GlobalSetFunction(e: Engine) : NativeFunction(e, "setGlobal", null) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("name", Type.String),
            ParameterDefinition("value", Type.MAny))

    override fun execute(parent: String, params: Map<String, Value>): Any? {
        engine.mem[engine.getValue(parent, params["name"]!!).value as String] = engine.getValue(parent, params["value"]!!)
        return null
    }


}