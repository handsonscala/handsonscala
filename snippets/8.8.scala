@ println(small)
[{"hello":"world","answer":42},true]

@ small(0)("hello") = "goodbye"

@ small(0)("tags") = ujson.Arr("awesome", "yay", "wonderful")

@ println(small)
[{"hello":"goodbye","answer":42,"tags":["awesome","yay","wonderful"]},true]
