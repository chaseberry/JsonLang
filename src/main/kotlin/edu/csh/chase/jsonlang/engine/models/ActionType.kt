package edu.csh.chase.jsonlang.engine.models

import java.util.*

class ActionType(val parameters: Map<String, Type>, val returns: RawType?) : Type(RawType.Action)