package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.Core.PrintFunction
import edu.csh.chase.jsonlang.engine.exceptions.JLRuntimeException
import edu.csh.chase.jsonlang.engine.models.*
import edu.csh.chase.jsonlang.engine.models.Function
import java.util.*

abstract class Engine(val programs: ArrayList<Program>, initWithStdLib: Boolean) {

    val stack = LinkedList<Frame>()

    val mem = HashMap<String, Any>()
    val functions = HashMap<String, NativeFunction>()

    init {
        //init core functions
        functions["print"] = PrintFunction()
    }

    abstract fun execute()

    fun executeFunction(prog: Program, function: Function, params: ArrayList<Parameter>? = null) {
        stack.push(Frame(prog.name, function.name, null))
        params?.forEach {
            mem["${prog.name}.${function.name}.${it.name}"] = it.value
        }
        function.actions.forEach {
            executeAction(prog, function, it)
        }
        params?.forEach {
            mem.remove("${prog.name}.${function.name}.${it.name}")
        }
        stack.pop()
    }

    fun executeAction(prog: Program, function: Function, action: Action) {
        stack.push(Frame(prog.name, function.name, action.name))
        val func = findFunction(action.name)
        if (func is NativeFunction) {
            executeCoreFunction(prog, func, action.parameters)
        } else {

        }
        stack.pop()
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

    fun executeCoreFunction(prog: Program, func: NativeFunction, params: Map<String, Value>) {
        stack.push(Frame(prog.name, func.name))
        val builtParams = ArrayList<Any?>()
        if (params.size != func.params.size) {
            throw JLRuntimeException("Error executing core function ${func.name}. " +
                    "Expected ${func.params.size} parameters. Got ${params.size}.")
        }
        func.params.forEach {
            val v = params[it.name] ?: throw JLRuntimeException("Error executing core function ${func.name}. " +
                    "Missing parameter ${it.name}")
            
            if (!v.isAcceptedType(it.type)) {
                throw JLRuntimeException("Error executing core function ${func.name}. " +
                        "Parameter passed to ${it.name} was incorrect. Expected ${it.type}, got ${v.type} ")
            }
            builtParams.add(v.value)
        }
        func.execute(*builtParams.toArray())
        stack.pop()
    }

}