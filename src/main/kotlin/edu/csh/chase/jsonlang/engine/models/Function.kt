package edu.csh.chase.jsonlang.engine.models

import java.util.*

data class Function(val name: String,
               val parameters: ArrayList<ParameterDefinition>, //Empty array if it doesn't exist
               val actions: ArrayList<Action>,
               val returns: Type?)
