@ val input = """local f = function(a) a + "1";  f("123")"""

@ evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty)
res40: Value = Str("1231")

@ val input = """local f = function(a, b) a + " " + b; f("hello", "world")"""

@ evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty)
res42: Value = Str("hello world")
