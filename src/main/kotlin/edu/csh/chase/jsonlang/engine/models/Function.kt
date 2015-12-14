package edu.csh.chase.jsonlang.engine.models

import java.util.*

class Function(val name: String,
               val parameters: ArrayList<ParameterDefinition>,
               val actions:ArrayList<Action>)
