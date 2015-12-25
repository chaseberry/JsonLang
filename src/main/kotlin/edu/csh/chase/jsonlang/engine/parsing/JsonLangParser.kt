package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.models.Program
import edu.csh.chase.kjson.JsonObject

class JsonLangParser(private val obj: JsonObject) {

    constructor(str: String) : this(JsonObject(str))

    fun parse(): Program {
        
    }

}