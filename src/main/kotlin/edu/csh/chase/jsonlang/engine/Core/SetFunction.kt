package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value
import java.util.*

class SetFunction(e: Engine) : NativeFunction(e, "set", null) {

    override val params: List<ParameterDefinition> = listOf(ParameterDefinition("name", Type.String),
            ParameterDefinition("value", Type.MAny))

    override fun execute(parent: String, vararg params: Value): Any? {
        engine.mem["$parent.${params[0].value as String}"] = params[1]
        return null
    }
}