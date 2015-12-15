package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.Core.AddFunction
import edu.csh.chase.jsonlang.engine.Core.PrintFunction
import edu.csh.chase.jsonlang.engine.Core.SetFunction
import edu.csh.chase.jsonlang.engine.exceptions.JLRuntimeException
import edu.csh.chase.jsonlang.engine.models.*
import edu.csh.chase.jsonlang.engine.models.Function
import java.util.*

abstract class Engine(val programs: ArrayList<Program>, initWithStdLib: Boolean) {

    val stack = LinkedList<Frame>()
    var `return`: Any? = null
    var returnType: Type? = null

    val mem = HashMap<String, Value>()
    val functions = HashMap<String, NativeFunction>()

    init {
        //init core functions
        functions["print"] = PrintFunction()
        functions["+"] = AddFunction()
        functions["set"] = SetFunction(mem)
    }

    abstract fun execute()

    fun executeFunction(parent: String, function: Function, params: ArrayList<Parameter>? = null) {
        stack.push(Frame("$parent.${function.name}"))
        params?.forEach {
            mem["$parent.${function.name}.${it.name}"] = it.value
        }
        function.actions.forEach {
            executeAction("$parent.${function.name}", it)
        }
        params?.forEach {
            mem.remove("$parent.${function.name}.${it.name}")
        }
        stack.pop()
    }

    fun executeAction(parent: String, action: Action) {
        stack.push(Frame("$parent.${action.name}"))
        val pair = findFunction(action.name)
        val func = pair.first
        if (func is NativeFunction) {
            executeNativeFunction("$parent.${action.name}", func, action.parameters)
        } else {
            executeFunction("$parent.${action.name}.${pair.second}", func as Function, parseParams(parent, func, action))
        }
        stack.pop()
    }

    fun findFunction(name: String): Pair<Any, String?> {
        val parts = name.split(".")
        if (parts.size == 1) {
            //Core function, no Program
            if (name in functions) {
                return functions[name]!! to null
            }
        } else {
            programs.forEach {
                if (it.name == parts[0]) {
                    return it.getFunction(parts[1]) to it.name
                }
            }
        }
        throw JLRuntimeException("Function $name not found. Did you forget a Program name?")

    }

    fun executeNativeFunction(parent: String, func: NativeFunction, params: Map<String, Value>) {
        stack.push(Frame("$parent.${func.name}"))
        val builtParams = ArrayList<Value>()
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
            builtParams.add(v)
        }

        `return` = func.execute(*builtParams.toTypedArray())
        returnType = func.returns

        stack.pop()
    }

    fun parseParams(parent: String, function: Function, action: Action): ArrayList<Parameter> {
        val builtParams = ArrayList<Parameter>()
        if (function.parameters.size != action.parameters.size) {
            throw JLRuntimeException("Error executing function $parent.${function.name}. " +
                    "Expected ${function.parameters.size} parameters, but got ${action.parameters.size}")
        }

        function.parameters.forEach {
            val v = action.parameters[it.name] ?: throw JLRuntimeException("Error executing function $parent.${function.name}. " +
                    "Missing parameter ${it.name}")
            if (!v.isAcceptedType(it.type)) {
                throw JLRuntimeException("Error executing function $parent.${function.name}. " +
                        "Parameter passed to ${it.name} was incorrect. Expected ${it.type}, got ${v.type} ")
            }
            builtParams.add(Parameter(it.name, v))
        }
        return builtParams
    }

    fun getValue(parent: String, v: Value): Value {
        val value = v.value
        if (value !is String) {
            return v
        }

        if (value == "return") {
            if (returnType == null) {
                throw JLRuntimeException("Error getting return at $parent. No return present")
            }
            return Value(`return`, returnType!!)
        }

        if ("$parent.$value" in mem) {
            return Value(mem["$parent.$value"], Type.MAny)
        }

        return v

    }

}