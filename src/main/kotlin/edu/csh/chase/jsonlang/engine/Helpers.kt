package edu.csh.chase.jsonlang.engine

import edu.csh.chase.jsonlang.engine.exceptions.JLParseException
import edu.csh.chase.jsonlang.engine.models.ParameterDefinition
import edu.csh.chase.jsonlang.engine.models.RawType
import edu.csh.chase.jsonlang.engine.models.Type
import edu.csh.chase.jsonlang.engine.models.Value

fun pd(name: String, type: Type) = ParameterDefinition(name, type)

fun v(value: Any?, type: Type) = Value(value, type)

fun booleanType() = Type(RawType.Boolean)

fun numType() = Type(RawType.Number)

fun stringType() = Type(RawType.String)

fun anyType() = Type(RawType.Any)

fun arrayType() = Type(RawType.Array)

fun mAnyType() = Type(RawType.MAny)

fun listType() = Type(RawType.Array)

fun actionType() = Type(RawType.Action)

fun parseError(error: String, location: String) = JLParseException(error, location)