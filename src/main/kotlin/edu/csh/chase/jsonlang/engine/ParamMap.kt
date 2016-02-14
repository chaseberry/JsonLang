package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.Action
import edu.csh.chase.jsonlang.engine.models.Value
import java.util.*

class ParamMap(private val parent: String, private val engine: Engine) : HashMap<String, Value>() {

    fun getBoolean(name: String): Boolean {
        return engine.getValue(parent, get(name)!!, name, booleanType()).value as Boolean
    }

    fun getString(name: String): String {
        return engine.getValue(parent, get(name)!!, name, stringType()).value as String
    }

    fun getInt(name: String): Int {
        return engine.getValue(parent, get(name)!!, name, numType()).value as Int
    }

    fun getDouble(name: String): Double {
        return engine.getValue(parent, get(name)!!, name, numType()).value as Double
    }

    fun getArray(name: String): List<Value> {
        return (engine.getValue(parent, get(name)!!, name, arrayType()).value as List<*>).filterIsInstance<Value>()
    }

    fun getAnyM(name: String): Any? {
        return engine.getValue(parent, get(name)!!, name, mAnyType()).value
    }

    fun getList(name: String): List<Any?> {
        return engine.getValue(parent, get(name)!!, name, listType()).value as List<Any?>
    }

    fun getAction(name: String): Action {
        return get(name)!!.value as Action
    }

    fun getValue(name: String): Value {
        return engine.getValue(parent, get(name)!!, name)
    }


}