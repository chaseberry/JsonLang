package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.Engine
import edu.csh.chase.jsonlang.engine.NativeFunction
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

class AddFunction(e: Engine) : NativeFunction(e, "+", Type.Any) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("i", Type.Any),
            ParameterDefinition("j", Type.Any))

    override fun execute(parent: String, params: Map<String, Value>): Any? {
        val i = engine.getValue(parent, params["i"]!!).value
        val j = engine.getValue(parent, params["j"]!!).value
        
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
                "i was ${params["i"]!!.type}, while j was ${params["j"]!!.type}")
    }


}