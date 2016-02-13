package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.InterpProgram

class SingleProgramEngine(val program: InterpProgram, initWithStdLid: Boolean = true) :
        Engine(listOf(program)) {

    override fun execute() {
        if (!program.containsFunction("main")) {
            throw error("${program.name} does not contain a function named 'main'")
        }
        val function = program.getFunction("main") ?: throw error("Function 'main' does not exist in ${program.name}")
        executeFunction("main", function)
    }


}