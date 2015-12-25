package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Value
import edu.csh.chase.jsonlang.engine.pd

class OrFunction(e: Engine) : NativeFunction(e, "or", RawType.Boolean) {

    override val parameters: List<ParameterDefinition> = listOf(pd("conditions", RawType.Array))

    override fun execute(parent: String, params: ParamMap): Value? {
        val listParams = params.getArray("conditions")
        listParams.forEachIndexed { i, v ->
            val b = engine.getValue("$parent.$name", v, i.toString(), RawType.Boolean)
            if (b.value == true) {
                return Value(true, RawType.Boolean)
            }
        }

        return Value(false, RawType.Boolean)
    }

}