package edu.csh.chase.jsonlang.engine.Core

import edu.csh.chase.jsonlang.engine.*
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Value

class AddFunction(e: Engine) : NativeFunction(e, "add", anyType()) {

    override val parameters: List<ParameterDefinition> = listOf(ParameterDefinition("i", anyType()),
            ParameterDefinition("j", anyType()))

    override fun execute(parent: String, params: ParamMap): Value? {
        val i = params.getAnyM("i")
        val j = params.getAnyM("j")

        if (i is Int && j is Int) {
            return v(i + j, numType())
        }

        if (i is Double && j is Double) {
            return v(i + j, numType())
        }

        if (i is String && j is String) {
            return v(i + j, stringType())
        }

        if (i is Double && j is Int) {
            return v(i + j.toDouble(), numType())
        }

        if (i is Int && j is Double) {
            return v(i.toDouble() + j, numType())
        }

        if (i is Boolean && j is Boolean) {
            return v(i || j, booleanType())
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