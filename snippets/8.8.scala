> println(small)
[{"hello":"world","answer":42},true]

> small(0)("hello") = "goodbye"

> small(0)("tags") = ujson.Arr("cool", "yay", "nice")

> println(small)
[{"hello":"goodbye","answer":42,"tags":["cool","yay","nice"]},true]
