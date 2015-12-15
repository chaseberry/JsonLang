package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.parsing.Parser
import edu.csh.chase.kjson.Json

fun main(args: Array<String>) {

    val program = Parser.parseProgram(Json(
            "name" to "main",
            "functions" to Json[
                    Json(
                            "name" to "main",
                            "actions" to Json[
                                    Json(
                                            "name" to "print",
                                            "parameters" to Json[
                                                    Json(
                                                            "name" to "message",
                                                            "value" to "Hello World!"
                                                    )
                                                    ]
                                    )
                                    ]
                    )
                    ]
    ))

    SingleProgramEngine(program).execute()

}