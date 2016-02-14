package edu.csh.chase.jsonlang.engine.stdlib

import edu.csh.chase.jsonlang.engine.*
import edu.csh.chase.jsonlang.engine.models.Action
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Value
import edu.csh.chase.jsonlang.engine.parsing.JsonLangValueParser
import java.util.*

class Filter(e: Engine) : NativeFunction(e, "filter", listType()) {

    override val parameters: List<ParameterDefinition> = listOf(pd("list", listType()), pd("predicate", actionType()))

    override fun execute(parent: String, params: ParamMap): Value? {

        val lst = params.getList("list")

        val retList = ArrayList<Any?>()

        lst.forEach {
            val action = params.getAction("predicate")
            val actionParams = HashMap(action.parameters)
            actionParams["element"] = JsonLangValueParser(it, parent).value
            val act = Action(action.name, actionParams)
            val v = engine.getValue(parent, Value(act, actionType()), "element", booleanType())
            if ((v.value as Boolean)) {
                retList.add(it)
            }
        }

        return Value(retList, listType())
    }


}