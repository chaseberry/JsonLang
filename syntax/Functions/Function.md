###Function

local defines who can access this function
* false - anyone can access
* true - only the parent can access this


```json
{
    "name" : String,
    "local" : ?Boolean,
    "arguments" : ?List<Arugment>,
    "returns": ?Type,
    "do" : List<Action>,
    
}
```