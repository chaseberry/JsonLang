package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value
import java.util.*

class ParamMap(private val parent: String, private val engine: Engine) : HashMap<String, Value>() {

    fun getBoolean(name: String): Boolean {
        return engine.getValue(parent, get(name)!!, name, Type.Boolean).value as Boolean
    }

    fun getString(name: String): String {
        return engine.getValue(parent, get(name)!!, name, Type.String).value as String
    }

    fun getInt(name: String): Int {
        return engine.getValue(parent, get(name)!!, name, Type.Number).value as Int
    }

    fun getDouble(name: String): Double {
        return engine.getValue(parent, get(name)!!, name, Type.Number).value as Double
    }

    fun getArray(name: String): List<Value> {
        return (engine.getValue(parent, get(name)!!, name, Type.Array).value as List<*>).filterIsInstance<Value>()
    }

    fun getAnyM(name: String): Any? {
        return engine.getValue(parent, get(name)!!, name, Type.MAny).value
    }

    fun getValue(name: String): Value {
        return engine.getValue(parent, get(name)!!, name)
    }

}