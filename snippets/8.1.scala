> val output = ujson.Arr(
    ujson.Obj("hello" -> "world", "answer" -> 42),
    true
  )

> output(0)("hello") = "goodbye"

> output(0)("tags") = ujson.Arr("cool", "yay", "nice")

> println(output)
[{"hello":"goodbye","answer":42,"tags":["cool","yay","nice"]},true]
