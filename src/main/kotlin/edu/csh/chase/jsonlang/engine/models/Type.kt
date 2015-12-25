package edu.csh.chase.jsonlang.engine.models

open class Type(val type: RawType) {

    override fun equals(other: Any?): Boolean {
        return other is Type && other.type == type
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    override fun toString(): String = type.toString()

}