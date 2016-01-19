package edu.csh.chase.jsonlang.engine.models

class ActionType(val parameters: Map<String, Type>, val returns: RawType?) : Type(RawType.Action)