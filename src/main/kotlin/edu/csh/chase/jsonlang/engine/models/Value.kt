package edu.csh.chase.jsonlang.engine.models

//Type is gotten from the value
data class Value(val value: Any?,
            val type: Type) {

    fun isAcceptedType(requiredType: Type): Boolean {
        return when (requiredType) {
            Type.MAny -> true
            Type.Any -> (type == Type.Any || type == Type.Array)
            Type.String -> type == Type.String
            Type.MString -> type == Type.String || type == Type.MString
            else -> throw RuntimeException("I need to finish this")
        }
    }

}