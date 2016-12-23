##Types

The following basic types are supported by JL  
* Boolean
* Number
* String
* List
* Object
* Any

Lists have a homogeneous type.  
All types are derived from the `Any` type.
All classes are valid types as well.  

####Nullability  
All types in JL are non-nullable by default.  
A type may become option by being prefixed with a `?`

* ?Boolean
* ?Number
* ?String
* ?List
* ?Object
* ?Any

Using `?Any` is akin to anything possible

#####Numbers
There are two sub-types of numbers
* Int
* Double

Both of these can be used as types