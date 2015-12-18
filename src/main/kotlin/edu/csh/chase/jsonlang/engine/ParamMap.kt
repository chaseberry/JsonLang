package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.Value
import java.util.*

class ParamMap(private val parent: String, private val engine: Engine) : HashMap<String, Value>() {

    fun getBoolean(name: String): Boolean {
        return engine.getValue(parent, get(name)!!).value as Boolean
    }

    fun getString(name: String): String {
        return engine.getValue(parent, get(name)!!).value as String
    }

    fun getInt(name: String): Int {
        return engine.getValue(parent, get(name)!!).value as Int
    }

    fun getDouble(name: String): Double {
        return engine.getValue(parent, get(name)!!).value as Double
    }

    fun getAnyM(name: String): Any? {
        return engine.getValue(parent, get(name)!!).value
    }

    fun getValue(name: String): Value {
        return engine.getValue(parent, get(name)!!)
    }

}