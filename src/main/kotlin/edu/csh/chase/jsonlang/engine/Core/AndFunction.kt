package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.*
import edu.csh.chase.jsonlang.engine.models.GenericType
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Value

class AndFunction(e: Engine) : NativeFunction(e, "and", booleanType()) {

    override val parameters: List<ParameterDefinition> = listOf(pd("conditions", GenericType(RawType.Array, RawType.Boolean)))

    override fun execute(parent: String, params: ParamMap): Value? {
        val listParams = params.getArray("conditions")
        listParams.forEachIndexed { i, v ->
            val b = engine.getValue("$parent.$name", v, i.toString(), booleanType())
            if (b.value == false) {
                return Value(false, booleanType())
            }
        }

        return Value(true, booleanType())
    }

}