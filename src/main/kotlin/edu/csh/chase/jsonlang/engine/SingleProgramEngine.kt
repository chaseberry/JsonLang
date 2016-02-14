package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.InterpProgram
import edu.csh.chase.jsonlang.engine.stdlib.Filter

class SingleProgramEngine(val program: InterpProgram) : Engine(arrayListOf(program)) {

    val stdlib = NativeProgram("stdlib", listOf(Filter(this)))

    override fun execute() {
        if (!program.containsFunction("main")) {
            throw error("${program.name} does not contain a function named 'main'")
        }

        addProgram(stdlib)

        val function = program.getFunction("main") ?: throw error("Function 'main' does not exist in ${program.name}")
        executeFunction("main", function)
    }


}