package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value
import edu.csh.chase.jsonlang.engine.pd

class OrFunction(e: Engine) : NativeFunction(e, "||", Type.Boolean) {

    override val parameters: List<ParameterDefinition> = listOf(pd("conditions", Type.Array))

    override fun execute(parent: String, params: ParamMap): Value? {
        val listParams = params.getArray("conditions")
        listParams.forEachIndexed { i, v ->
            val b = engine.getValue("$parent.$name", v, i.toString(), Type.Boolean)
            if (b.value == true) {
                return Value(true, Type.Boolean)
            }
        }

        return Value(false, Type.Boolean)
    }

}