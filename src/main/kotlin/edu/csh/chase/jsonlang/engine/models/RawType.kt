package edu.csh.chase.jsonlang.engine.models

enum class RawType(val value: kotlin.String) {
    String("String"), Number("Number"), Boolean("Boolean"), Object("Object"), Array("Array"), Any("Any"),
    MString("?String"), MNumber("?Number"), MBoolean("?Boolean"), MObject("?Object"), MArray("?Array"), MAny("?Any"),
    Action("action");

    override fun toString(): kotlin.String = value

    fun isParentType(requiredType: RawType): kotlin.Boolean {
        return when (requiredType) {
            RawType.MAny -> true
            RawType.Any -> (this == RawType.Any || this == RawType.Array || this == RawType.String || this == RawType.Object ||
                    this == RawType.Number || this == RawType.Boolean)
            RawType.String -> this == RawType.String
            RawType.MString -> this == RawType.String || this == RawType.MString
            RawType.Number -> this == RawType.Number
            RawType.MNumber -> this == RawType.MNumber || this == RawType.Number
            RawType.Boolean -> this == RawType.Boolean
            RawType.MBoolean -> this == RawType.Boolean || this == RawType.MBoolean
            RawType.Array -> this == RawType.Array
            RawType.MArray -> this == RawType.Array || this == RawType.MArray
            RawType.Object -> this == RawType.Object
            RawType.MObject -> this == RawType.Object || this == RawType.MObject
            RawType.Action -> this == RawType.Action
            else -> throw RuntimeException("I need to finish this")
        }
    }

}