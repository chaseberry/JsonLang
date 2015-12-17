package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

class GlobalGetFunction(e: Engine) : NativeFunction(e, "getGlobal", Type.MAny) {
    override val parameters: List<ParameterDefinition>
        get() = listOf(ParameterDefinition("name", Type.String))

    override fun execute(parent: String, params: Map<String, Value>): Any? {
        val name = engine.getValue(parent, params["name"]!!).value as String
        if (name !in engine.mem) {
            throw engine.error("Error executing native function ${this.name} at $parent.${this.name}. " +
                    "$name is not defined in this memspace")
        }
        return engine.mem[name]
    }
}