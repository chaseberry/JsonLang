package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.exceptions.JLRuntimeException
import edu.csh.chase.jsonlang.engine.models.*
import edu.csh.chase.jsonlang.engine.models.Function
import edu.csh.chase.jsonlang.engine.parsing.CoreLoader
import edu.csh.chase.jsonlang.engine.parsing.Parser
import edu.csh.chase.kjson.JsonObject
import java.util.*

abstract class Engine(val programs: ArrayList<Program>, initWithStdLib: Boolean) {

    val stack = LinkedList<Frame>()

    val mem = HashMap<String, Value>()
    val coreFunctions = HashMap<String, NativeFunction>()

    init {
        //init core functions
        CoreLoader.loadCoreFunctions().forEach {
            val func = it.getDeclaredConstructor(Engine::class.java).newInstance(this)
            coreFunctions[func.name] = func
        }
    }

    abstract fun execute()

    fun executeFunction(parent: String, function: Function, params: Map<String, Value>? = null): Value? {
        stack.push(Frame("$parent.${function.name}"))
        params?.forEach {
            //TODO make this load lazily?
            val v = getValue(parent, it.value)
            if (v.type != it.value.type) {
                throw error("$parent.${function.name} parameter expected type ${it.value.type}. Got ${v.type}")
            }
            mem["$parent.${function.name}.${it.key}"] = v
        }
        var currentVal: Value? = null
        function.actions.forEach {
            currentVal = executeAction("$parent.${function.name}", it)
        }
        params?.forEach {
            mem.remove("$parent.${function.name}.${it.key}")
        }

        if (currentVal == null && function.returns != null) {
            throw error("$parent.${function.name} returns ${function.returns}. No value was returned from last action.")
        }

        if (currentVal?.type != function.returns) {
            throw error("$parent.${function.name} returns ${function.returns}. Got ${currentVal?.type}")
        }

        stack.pop()
        return if (function.returns == null) null else currentVal
    }

    private fun executeAction(parent: String, action: Action): Value? {
        val func = findFunction(action.name)
        val r = if (func is NativeFunction) {
            executeNativeFunction("$parent", func, action)
        } else {
            executeFunction("$parent", func as Function, parseParams(parent, func, action))
        }
        return r
    }

    private fun findFunction(name: String): Callable {
        val parts = name.split(".")
        if (parts.size == 1) {
            //Core function, no Program
            if (name in coreFunctions) {
                return coreFunctions[name]!!
            }
        } else {
            programs.forEach {
                if (it.name == parts[0]) {
                    val func = it.getFunction(parts[1]) ?: throw error("Function $name does not exist in ${it.name}")
                    return func
                }
            }
        }
        throw error("Function $name not found. Did you forget a Program name?")

    }

    private fun executeNativeFunction(parent: String, func: NativeFunction, action: Action): Value? {
        stack.push(Frame("$parent.${func.name}"))

        val params = parseParams(parent, func, action)

        val r = func.execute(parent, params)

        stack.pop()
        return if (func.returns == null) null else Value(r, func.returns!!)
    }

    private fun parseParams(parent: String, function: Callable, action: Action): Map<String, Value> {
        if (function.parameters.size != action.parameters.size) {
            throw error("Error executing function $parent.${function.name}. " +
                    "Expected ${function.parameters.size} parameters, but got ${action.parameters.size}")
        }

        return function.parameters.toMap({ it.name }) {
            val v = action.parameters[it.name] ?: throw error("Error executing function $parent.${function.name}. " +
                    "Missing parameter ${it.name}")

            var checkedType = v.type

            if (v.value is JsonObject) {
                val act = Parser.unsafeParseAction(v.value)//Is the passed parameter actually an action?

                if (act != null) {
                    val ret = getReturnType(act)//Get the function it calls return type
                    if (ret != null) {
                        checkedType = ret
                    }
                }
            }

            if (!checkedType.isParentType(it.type)) {
                throw error("Error executing function $parent.${function.name}. " +
                        "Parameter passed to ${it.name} was incorrect. Expected ${it.type}, got ${v.type} ")
            }

            action.parameters[it.name]!!
        }

    }

    fun getReturnType(action: Action): Type? {
        try {
            val func = findFunction(action.name)
            return func.returns
        } catch(e: JLRuntimeException) {

        }
        return null
    }

    fun getValue(parent: String, v: Value): Value {
        val value = v.value

        //TODO allow str interop, by spliting on 'space' and check each token for *name
        if (value is String) {
            val valParts = value.split(" ")
            if (valParts.size == 1 && valParts[0][0] == '*') {
                val varName = valParts[0].substring(1)
                if ("$parent.$varName" !in mem) {
                    throw error("$varName does not exist in this memory space.")
                }
                return mem["$parent.$varName"]!!
            } else {
                var newValue = ""
                valParts.forEach {
                    val varName = it.substring(1)
                    if (it[0] == '*') {
                        if ("$parent.$varName" in mem) {
                            newValue += " ${mem["$parent.$varName"]!!.value}"
                        } else {
                            newValue += " $it"
                        }
                    } else {
                        newValue += " $it"
                    }
                }
                return Value(newValue.substring(1), Type.String)
            }
        }

        if (value is JsonObject) {
            val action = Parser.unsafeParseAction(value) ?: return v
            val value = executeAction(parent, action) ?: return v
            return value
        }

        return v

    }

    fun error(message: String): JLRuntimeException {
        return JLRuntimeException(mem, stack, message)
    }

}