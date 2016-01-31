package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.Value
import java.util.*

data class Frame(val location: String, val memory: HashMap<String, Value> = HashMap()) {


}