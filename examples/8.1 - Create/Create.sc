val output = ujson.Arr(
  ujson.Obj("hello" -> ujson.Str("world"), "answer" -> ujson.Num(42)),
  ujson.Bool(true)
)

println(output)

val output2 = ujson.Arr(
  ujson.Obj("hello" -> "world", "answer" -> 42),
  true
)

println(output2)

assert(
  pprint.log(ujson.write(output)) ==
  """[{"hello":"world","answer":42},true]"""
)

pprint.log(
  ujson.write(output, indent = 4) ==
  """[
    |    {
    |        "hello": "world",
    |        "answer": 42
    |    },
    |    true
    |]""".stripMargin
)
