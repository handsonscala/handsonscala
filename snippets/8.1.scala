@ val output = ujson.Arr(
    ujson.Obj("hello" -> "world", "answer" -> 42),
    true
  )

@ output(0)("hello") = "goodbye"

@ output(0)("tags") = ujson.Arr("awesome", "yay", "wonderful")

@ println(output)
[{"hello":"goodbye","answer":42,"tags":["awesome","yay","wonderful"]},true]
