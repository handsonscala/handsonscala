@ val input = """local greeting = "Hello "; greeting + greeting"""

@ evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty)
res33: Value = Str("Hello Hello ")

@ val input = """local x = "Hello "; local y = "world"; x + y"""

@ evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty)
res35: Value = Str("Hello world")
