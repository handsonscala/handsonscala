> evaluate(fastparse.parse("\"hello\"", Parser.expr(using _)).get.value, Map.empty)
res12: Value = Str("hello")

> val input = """{"hello": "world", "key": "value"}"""

> evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty)
res13: Value = Dict(Map("hello" -> Str("world"), "key" -> Str("value")))
