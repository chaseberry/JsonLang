package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.exceptions.JLRuntimeException
import edu.csh.chase.jsonlang.engine.parsing.Parser
import edu.csh.chase.kjson.JsonObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun main(args: Array<String>) {

    val reader = BufferedReader(FileReader(File("/Users/chase/Code/JsonLang/src/main/kotlin/edu/csh/chase/jsonlang/engine/test.json")))
    val json = JsonObject(reader.readText())
    reader.close()

    val program = Parser.parseProgram(json)

    try {
        SingleProgramEngine(program).execute()

    } catch(e: JLRuntimeException) {
        println("Error occurred running ${program.name}")
        println("Error: ${e.message}")
        println("Stack: ${e.stack}")
        println("Memory: ${e.mem}")
    }

}