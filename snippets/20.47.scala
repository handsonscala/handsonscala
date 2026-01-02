> val input = """local f = function(a) a + "1";  f("123")"""

> evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty)
res19: Value = Str("1231")

> val input = """local f = function(a, b) a + " " + b; f("hello", "world")"""

> evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty)
res20: Value = Str("hello world")
