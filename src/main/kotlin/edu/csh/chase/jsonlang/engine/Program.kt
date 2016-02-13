package edu.csh.chase.jsonlang.engine

abstract class Program<T : Callable>(val name: String,
                                     val functions: List<T>) {

    fun containsFunction(name: String): Boolean {
        functions.forEach { if (it.name == name) return true }
        return false
    }

    fun getFunction(name: String): T? {
        functions.forEach { if (it.name == name) return it }
        return null
    }

}