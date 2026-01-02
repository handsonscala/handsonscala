> val input = """local greeting = "Hello "; greeting + greeting"""

> evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty)
res16: Value = Str("Hello Hello ")

> val input = """local x = "Hello "; local y = "world"; x + y"""

> evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty)
res17: Value = Str("Hello world")
