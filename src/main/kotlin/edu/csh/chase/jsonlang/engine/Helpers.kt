package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Value

fun pd(name: String, type: RawType) = ParameterDefinition(name, type)

fun v(value:Any?, type: RawType) = Value(value, type)