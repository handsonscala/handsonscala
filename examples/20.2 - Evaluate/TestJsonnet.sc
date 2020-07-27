import $file.Evaluate, Evaluate._
import $file.Values, Values._
import $file.Parse, Parse._

assert(
  evaluate(fastparse.parse("\"hello\"", Parser.expr(_)).get.value, Map.empty) ==
  Value.Str("hello")
)

val input = """{"hello": "world", "key": "value"}"""

assert(
  evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty) ==
  Value.Dict(Map("hello" -> Value.Str("world"), "key" -> Value.Str("value")))
)

assert(
  evaluate(fastparse.parse("\"hello\" + \"world\"", Parser.expr(_)).get.value, Map.empty) ==
  Value.Str("helloworld")
)

val input2 = """local greeting = "Hello "; greeting + greeting"""

assert(
  evaluate(fastparse.parse(input2, Parser.expr(_)).get.value, Map.empty) ==
  Value.Str("Hello Hello ")
)
val input3 = """local x = "Hello "; local y = "world"; x + y"""

assert(
  evaluate(fastparse.parse(input3, Parser.expr(_)).get.value, Map.empty) ==
  Value.Str("Hello world")
)

val input4 = """local f = function(a) a + "1";  f("123")"""

assert(
  evaluate(fastparse.parse(input4, Parser.expr(_)).get.value, Map.empty) ==
  Value.Str("1231")
)

val input5 = """local f = function(a, b) a + " " + b; f("hello", "world")"""

assert(
  evaluate(fastparse.parse(input5, Parser.expr(_)).get.value, Map.empty) ==
  Value.Str("hello world")
)
