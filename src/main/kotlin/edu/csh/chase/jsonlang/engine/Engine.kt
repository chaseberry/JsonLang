package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.exceptions.JLRuntimeException
import edu.csh.chase.jsonlang.engine.models.Action
import edu.csh.chase.jsonlang.engine.models.Function
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value
import edu.csh.chase.jsonlang.engine.parsing.CoreLoader
import java.util.*

abstract class Engine(val programs: ArrayList<Program<out Callable>>) {

    private val stack = LinkedList<Frame>()

    private val globalMemory = HashMap<String, Value>()

    private val coreFunctions = HashMap<String, NativeFunction>()

    init {
        //init core functions
        CoreLoader.loadCoreFunctions().forEach {
            val func = it.getDeclaredConstructor(Engine::class.java).newInstance(this)
            coreFunctions[func.name] = func
        }
    }

    fun addProgram(program: Program<out Callable>) {
        if (program in programs) {
            throw error("Error adding ${program.name}. Program already exists.")
        }
        programs.add(program)
    }

    abstract fun execute()

    fun setFrameMemoryValue(name: String, value: Value) {
        val frame = stack[1]
        frame.memory[name] = value
    }

    fun getFrameMemoryValue(name: String): Value {
        stack.forEach {
            if (name in it.memory) {
                return it.memory[name]!!
            }
        }
        throw error("$name was not found.")
    }

    fun removeFrameMemoryValue(name: String) {
        stack.forEach {
            if (name in it.memory) {
                it.memory.remove(name)
            }
        }
        throw error("$name was not found.")
    }

    fun setGlobalMemoryValue(name: String, value: Value) {
        globalMemory[name] = value
    }

    fun getGlobalMemoryValue(name: String): Value {
        return globalMemory[name] ?: throw error("$name was not found in global memory.")
    }

    fun removeGlobalMemoryValue(name: String) {
        if (name in globalMemory) {
            globalMemory.remove(name)
            return
        }
        throw error("$name was not found in global memory.")
    }

    fun executeFunction(parent: String, function: Function, params: Map<String, Value>? = null): Value? {
        stack.push(Frame("$parent.${function.name}"))
        params?.forEach {
            //TODO make this load lazily?
            val v = getValue("$parent.${function.name}", it.value, it.key)
            if (v.type != it.value.type) {
                throw error("$parent.${function.name} parameter expected type ${it.value.type}. Got ${v.type}")
            }
            setFrameMemoryValue(it.key, v)
        }
        var currentVal: Value? = null
        function.actions.forEach {
            currentVal = executeAction("$parent.${function.name}", it)
        }
        params?.forEach {
            removeFrameMemoryValue(it.key)
        }

        if (currentVal == null && function.returns != null) {
            throw error("$parent.${function.name} returns ${function.returns}. No value was returned from last action.")
        }

        if (currentVal != null && function.returns != null && currentVal!!.type.type.isParentType(function.returns.type)) {
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

    internal fun executeAction(parent: String, action: Action): Value? {
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
            getFrameMemoryValue(varName)
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

    fun error(message: String): JLRuntimeException {
        return JLRuntimeException(globalMemory, stack, message)
    }

}