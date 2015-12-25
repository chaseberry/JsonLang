package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.ParamMap
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Value
import edu.csh.chase.jsonlang.engine.v

class AddFunction(e: Engine) : NativeFunction(e, "add", RawType.Any) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("i", RawType.Any),
            ParameterDefinition("j", RawType.Any))

    override fun execute(parent: String, params: ParamMap): Value? {
        val i = params.getAnyM("i")
        val j = params.getAnyM("j")

        if (i is Int && j is Int) {
            return v(i + j, RawType.Number)
        }

        if (i is Double && j is Double) {
            return v(i + j, RawType.Number)
        }

        if (i is String && j is String) {
            return v(i + j, RawType.String)
        }

        if (i is Double && j is Int) {
            return v(i + j.toDouble(), RawType.Number)
        }

        if (i is Int && j is Double) {
            return v(i.toDouble() + j, RawType.Number)
        }

        if (i is Boolean && j is Boolean) {
            return v(i || j, RawType.Boolean)
        }

        /**
        // Need to implement jsonArray, and jsonObject + function
        if (i is JsonArray && j is JsonArray) {
        return JsonArray(i)
        }

        if (i is JsonObject && j is JsonObject) {
        return i + j
        }
         */
        throw engine.error("Error executing native function $name at $parent.$name. i and j were incompatible types. " +
                "i was ${params["i"]!!.type}, while j was ${params["j"]!!.type}")
    }


}