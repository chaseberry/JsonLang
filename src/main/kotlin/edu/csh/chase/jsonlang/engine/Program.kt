package edu.csh.chase.jsonlang.engine

abstract class Program<T : Callable>(val name: String,
                                     val functions: List<T>) {

    override operator fun equals(other: Any?): Boolean {
        return other is Program<out Callable> && other.name == name
    }

    fun containsFunction(name: String): Boolean {
        functions.forEach { if (it.name == name) return true }
        return false
    }

    fun getFunction(name: String): T? {
        functions.forEach { if (it.name == name) return it }
        return null
    }

    override fun hashCode(): Int{
        var result = name.hashCode()
        result += 31 * result + functions.hashCode()
        return result
    }

}