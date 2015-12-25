package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.exceptions.ParseException
import edu.csh.chase.jsonlang.engine.models.Program
import edu.csh.chase.kjson.JsonObject

class JsonLangParser(private val obj: JsonObject) {

    constructor(str: String, parent: String) : this(JsonObject(str))

    val program: Program by lazy {
        parse()
    }

    fun parse(): Program {
        val name = obj.getString("name") ?: throw ParseException("No 'name' given to Program")
        val arr = obj.getJsonArray("functions") ?: throw ParseException("No 'functions' array in program $name")
        val functions = arr.map {
            if (it !is JsonObject) {
                throw ParseException("A 'functions' object must be an object in program $name")
            }
            JsonLangFunctionParser(it, "$name").function
        }.toArrayList()
        return Program(name, functions)
    }

}