package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

fun pd(name: String, type: Type) = ParameterDefinition(name, type)

fun v(value:Any?, type:Type) = Value(value, type)