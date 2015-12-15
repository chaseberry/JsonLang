package edu.csh.chase.jsonlang.engine.exceptions

import edu.csh.chase.jsonlang.engine.Frame
import edu.csh.chase.jsonlang.engine.models.Value
import java.util.*

class JLRuntimeException(val mem: HashMap<String, Value>, val stack: LinkedList<Frame>,
                         message: String) : Exception(message)