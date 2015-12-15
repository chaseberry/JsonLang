package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.exceptions.JLRuntimeException
import edu.csh.chase.jsonlang.engine.models.Program

class SingleProgramEngine(val program: Program, initWithStdLid: Boolean = true) :
        Engine(arrayListOf(program), initWithStdLid) {

    override fun execute() {
        if (!program.containsFunction("main")) {
            throw JLRuntimeException("${program.name} does not contain a function named 'main'")
        }

        val function = program.getFunction("main")
        executeFunction(function)
    }


}