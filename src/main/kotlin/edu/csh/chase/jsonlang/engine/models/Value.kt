package edu.csh.chase.jsonlang.engine.models

//Type is gotten from the value
data class Value(val value: Any?,
                 val type: Type) {

    fun isAcceptedType(requiredType: Type): Boolean {
        return when (requiredType) {
            Type.MAny -> true
            Type.Any -> (type == Type.Any || type == Type.Array || type == Type.String || type == Type.Object || type == Type.Number || type == Type.Boolean)
            Type.String -> type == Type.String
            Type.MString -> type == Type.String || type == Type.MString
            Type.Number -> type == Type.Number
            Type.MNumber -> type == Type.MNumber || type == Type.Number
            Type.Boolean -> type == Type.Boolean
            Type.MBoolean -> type == Type.Boolean || type == Type.MBoolean
            else -> throw RuntimeException("I need to finish this")
        }
    }

}