package edu.csh.chase.jsonlang.engine.exceptions

class JLParseException(message: String, val location: String) : Exception(message)