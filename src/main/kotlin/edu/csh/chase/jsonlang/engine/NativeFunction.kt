package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

abstract class NativeFunction(val name: String, val returns: Type?) {

    abstract val params: List<ParameterDefinition>
    
    abstract fun execute(vararg params: Value): Any?

}