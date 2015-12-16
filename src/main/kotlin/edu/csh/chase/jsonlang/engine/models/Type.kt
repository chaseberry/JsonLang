package edu.csh.chase.jsonlang.engine.models

enum class Type(val value: String) {
    String("String"), Number("Number"), Boolean("Boolean"), Object("Object"), Array("Array"), Any("Any"),
    MString("?String"), MNumber("?Number"), MBoolean("?Boolean"), MObject("?Object"), MArray("?Array"), MAny("?Any");

    override fun toString(): kotlin.String = value

}