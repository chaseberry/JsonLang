package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.exceptions.JLRuntimeException
import edu.csh.chase.jsonlang.engine.models.*
import edu.csh.chase.jsonlang.engine.models.Function
import edu.csh.chase.jsonlang.engine.parsing.CoreLoader
import java.util.*

abstract class Engine(val programs: ArrayList<Program>, initWithStdLib: Boolean) {

    val stack = LinkedList<Frame>()//TODO Move the Memory into the stack

    val mem = HashMap<String, Value>()

    private val coreFunctions = HashMap<String, NativeFunction>()

    init {
        //init core functions
        CoreLoader.loadCoreFunctions().forEach {
            val func = it.getDeclaredConstructor(Engine::class.java).newInstance(this)
            coreFunctions[func.name] = func
        }
    }

    fun addFunction(func: NativeFunction) {
        if (func.name.contains(".")) {
            throw error("Error adding ${func.name}. Native function names cannot contains periods.")
        }
        if (func.name in coreFunctions) {
            throw error("Error adding ${func.name}. Function already exists.")
        }
        coreFunctions[func.name] = func
    }

    abstract fun execute()

    fun executeFunction(parent: String, function: Function, params: Map<String, Value>? = null): Value? {
        stack.push(Frame("$parent.${function.name}"))
        params?.forEach {
            //TODO make this load lazily?
            val v = getValue("$parent.${function.name}", it.value, it.key)
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

    private fun executeNativeFunction(parent: String, func: NativeFunction, action: Action): Value? {
        stack.push(Frame("$parent.${func.name}"))

        val params = parseParams(parent, func, action)

        val r = func.execute(parent, params)

        stack.pop()
        return r
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

    private fun parseParams(parent: String, function: Callable, action: Action): ParamMap {
        if (function.parameters.size != action.parameters.size) {
            throw error("Error executing function $parent.${function.name}. " +
                    "Expected ${function.parameters.size} parameters, but got ${action.parameters.size}")
        }

        val map = ParamMap("$parent.${function.name}", this)

        function.parameters.forEach {
            val v = action.parameters[it.name] ?: throw error("Error executing function $parent.${function.name}. " +
                    "Missing parameter ${it.name}")
            map.put(it.name, v)
        }

        return map
    }

    fun getValue(parent: String, v: Value, name: String): Value {
        return getValue(parent, v, name, null)
    }

    fun getValue(parent: String, v: Value, name: String, expectedType: Type?): Value {
        val value = v.value

        val returnedVal = if (value is String && value[0] == '*') {
            val varName = value.substring(1)
            val p = getBack(parent)
            mem["$p.$varName"] ?: throw error("'$p.$varName' does not exist in memory")
        } else if (value is Action) {
            executeAction(parent, value) ?: throw error("Error executing function $parent " +
                    "Parameter $name expected $expectedType. Got null from action ${value.name}")
        } else {
            v
        }

        if (expectedType != null && !returnedVal.type.type.isParentType(expectedType.type)) {
            throw error("Error executing function '$parent'. " +
                    "Parameter $name expected $expectedType. Got ${returnedVal.type}")
        }


        return returnedVal
    }

    private fun getBack(parent: String): String {
        var newStr = ""
        val parts = parent.split(".")
        parts.forEachIndexed { i, s ->
            if (i == parts.size - 1) {
                return@forEachIndexed
            }
            if (i != 0) {
                newStr += "."
            }
            newStr += s
        }
        return newStr
    }

    fun error(message: String): JLRuntimeException {
        return JLRuntimeException(mem, stack, message)
    }

}