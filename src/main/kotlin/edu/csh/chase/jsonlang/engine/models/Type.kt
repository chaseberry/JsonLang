package edu.csh.chase.jsonlang.engine.models

enum class Type(val value: String) {
    String("String"), Number("Number"), Boolean("Boolean"), Object("Object"), Array("Array"), Any("Any"),
    MString("?String"), MNumber("?Number"), MBoolean("?Boolean"), MObject("?Object"), MArray("?Array"), MAny("?Any"), Action("action");

    override fun toString(): kotlin.String = value


    fun isParentType(requiredType: Type): kotlin.Boolean {
        return when (requiredType) {
            Type.MAny -> true
            Type.Any -> (this == Type.Any || this == Type.Array || this == Type.String || this == Type.Object ||
                    this == Type.Number || this == Type.Boolean)
            Type.String -> this == Type.String
            Type.MString -> this == Type.String || this == Type.MString
            Type.Number -> this == Type.Number
            Type.MNumber -> this == Type.MNumber || this == Type.Number
            Type.Boolean -> this == Type.Boolean
            Type.MBoolean -> this == Type.Boolean || this == Type.MBoolean
            else -> throw RuntimeException("I need to finish this")
        }
    }

}