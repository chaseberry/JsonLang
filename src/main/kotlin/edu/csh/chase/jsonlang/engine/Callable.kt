package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.RawType

interface Callable {

    val parameters: List<ParameterDefinition>

    val name: String

    val returns: RawType?

}