package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type

class GlobalSetFunction(e: Engine) : NativeFunction(e, "setGlobal", null) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("name", Type.String),
            ParameterDefinition("value", Type.MAny))

    override fun execute(parent: String, params: ParamMap): Any? {
        engine.mem[engine.getValue(parent, params["name"]!!).value as String] = engine.getValue(parent, params["value"]!!)
        return null
    }


}