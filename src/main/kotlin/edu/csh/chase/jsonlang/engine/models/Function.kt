package edu.csh.chase.jsonlang.engine.models

import edu.csh.chase.jsonlang.engine.Callable
import java.util.*

data class Function(override val name: String,
                    override val parameters: List<ParameterDefinition>, //Empty array if it doesn't exist
                    val actions: ArrayList<Action>,
                    override val returns: RawType?) : Callable
