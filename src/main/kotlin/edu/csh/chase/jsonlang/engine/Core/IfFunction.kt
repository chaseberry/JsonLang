package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Value
import edu.csh.chase.jsonlang.engine.pd

class IfFunction(e: Engine) : NativeFunction(e, "if", RawType.MAny) {
    override val parameters: List<ParameterDefinition> = listOf(pd("condition", RawType.Boolean), pd("then", RawType.Any), pd("else", RawType.MAny))

    override fun execute(parent: String, params: ParamMap): Value? {
        return if (params.getBoolean("condition")) params.getValue("then") else params.getValue("else")

    }
}