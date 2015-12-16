package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

class AddFunction(e: Engine) : NativeFunction(e, "+", Type.Any) {

    override val params: List<ParameterDefinition> = listOf(ParameterDefinition("i", Type.Any),
            ParameterDefinition("j", Type.Any))

    override fun execute(parent: String, vararg params: Value): Any? {
        val i = params[0].value
        val j = params[1].value

        if (i is Int && j is Int) {
            return i + j
        }

        if (i is Double && j is Double) {
            return i + j
        }

        if (i is String && j is String) {
            return i + j
        }

        if (i is Double && j is Int) {
            return i + j.toDouble()
        }

        if (i is Int && j is Double) {
            return i.toDouble() + j
        }

        if (i is Boolean && j is Boolean) {
            return i || j
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
                "i was ${params[0].type}, while j was ${params[1].type}")
    }


}