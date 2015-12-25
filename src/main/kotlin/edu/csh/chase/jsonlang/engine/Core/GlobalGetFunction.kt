package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Value

class GlobalGetFunction(e: Engine) : NativeFunction(e, "getGlobal", RawType.MAny) {
    override val parameters: List<ParameterDefinition>
        get() = listOf(ParameterDefinition("name", RawType.String))

    override fun execute(parent: String, params: ParamMap): Value? {
        val name = params.getString("name")
        if (name !in engine.mem) {
            throw engine.error("Error executing native function ${this.name} at $parent.${this.name}. " +
                    "$name is not defined in this memspace")
        }
        return engine.mem[name]
    }
}