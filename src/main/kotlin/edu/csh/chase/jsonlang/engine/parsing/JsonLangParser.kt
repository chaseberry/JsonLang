package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.models.Function
import edu.csh.chase.jsonlang.engine.models.Program
import edu.csh.chase.jsonlang.engine.parseError
import edu.csh.chase.kjson.JsonObject

class JsonLangParser(private val obj: JsonObject) {

    constructor(str: String) : this(JsonObject(str))

    val program: Program by lazy {
        parse()
    }

    fun parse(): Program {
        val name = obj.getString("name") ?: throw parseError("No 'name' given to Program", "")
        val arr = obj.getJsonArray("functions") ?: throw parseError("No 'functions' array in program", name)
        val functions = arr.map {
            if (it !is JsonObject) {
                throw parseError("A 'function' must be an object in program", name)
            }
            JsonLangFunctionParser(it, "$name").function
        }.toCollection(arrayListOf<Function>())
        return Program(name, functions)
    }

}