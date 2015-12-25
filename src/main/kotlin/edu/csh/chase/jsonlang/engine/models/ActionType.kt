package edu.csh.chase.jsonlang.engine.models

class ActionType(val parameters: List<RawType>, val returns: RawType?) : Type(RawType.Action)