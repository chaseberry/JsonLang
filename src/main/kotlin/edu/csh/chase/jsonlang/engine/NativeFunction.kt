package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

abstract class NativeFunction(val engine: Engine, override val name: String, override val returns: Type?) : Callable {

    abstract override val parameters: List<ParameterDefinition>

    abstract fun execute(parent: String, params: ParamMap): Value?

    override fun equals(other: Any?): Boolean {
        return other is NativeFunction && other.name == name
    }

}