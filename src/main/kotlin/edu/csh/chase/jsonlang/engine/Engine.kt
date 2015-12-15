package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.Core.PrintFunction
import edu.csh.chase.jsonlang.engine.exceptions.JLRuntimeException
import edu.csh.chase.jsonlang.engine.models.Function
import edu.csh.chase.jsonlang.engine.models.Program
import edu.csh.chase.jsonlang.engine.models.Value
import java.util.*

abstract class Engine(val programs: ArrayList<Program>, initWithStdLib: Boolean) {

    val stack = LinkedList<Frame>()

    val mem = HashMap<String, Any>()
    val functions = HashMap<String, NativeFunction>()

    init {
        //Init core functions
        functions["print"] = PrintFunction()

    }

    abstract fun execute()

    fun executeFunction(function: Function, params: ArrayList<Any?>? = null) {
        function.actions.forEach {
            val func = findFunction(it.name)
            if (func is NativeFunction) {
                executeCoreFunction(func, it.parameters)
            } else {
                
            }
        }
    }

    fun findFunction(name: String): Any {
        val parts = name.split(".")
        if (parts.size == 1) {
            //Core function, no Program
            if (name in functions) {
                return functions[name]!!
            }
        } else {
            programs.forEach {
                if (it.name == parts[0]) {
                    return it.getFunction(parts[1])
                }
            }
        }
        throw JLRuntimeException("Function $name not found. Did you forget a Program name?")

    }

    fun executeCoreFunction(func: NativeFunction, params: Map<String, Value>) {
        val builtParams = ArrayList<Any?>()
        if (params.size != func.params.size) {
            throw JLRuntimeException("Expected ${func.params.size} parameters. Got ${params.size}")
        }
        func.params.forEach {
            val v = params[it.name] ?: throw JLRuntimeException("Missing parameter ${it.name}}")
            if (!v.isAcceptedType(it.type)) {
                throw JLRuntimeException("Parameter passed to ${it.name} was incorrect. Expected ${it.type}, got ${v.type} ")
            }
            builtParams.add(v.value)
        }
        func.execute(*builtParams.toArray())
    }

}