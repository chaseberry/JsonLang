package edu.csh.chase.jsonlang.engine.models

import java.util.*

data class Program(val name: String,
              val functions: ArrayList<Function>) {

    fun containsFunction(name: String): Boolean {
        functions.forEach { if (it.name == name) return true }
        return false
    }

    fun getFunction(name: String): Function? {
        functions.forEach { if (it.name == name) return it }
        return null
    }

}