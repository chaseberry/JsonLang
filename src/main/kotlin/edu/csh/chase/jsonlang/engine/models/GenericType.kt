package edu.csh.chase.jsonlang.engine.models

class GenericType(type: RawType, val generic: RawType) : Type(type) {

    override fun equals(other: Any?): Boolean {
        return other is GenericType && other.type == type && other.generic == generic
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result += 31 * result + generic.hashCode()
        return result
    }

}