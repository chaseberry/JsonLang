package edu.csh.chase.jsonlang.engine.models

data class Action(val name: String,
             val parameters: Map<String, Value>) {
}