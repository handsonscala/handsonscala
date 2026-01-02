> val input = """local greeting = "Hello "; greeting + greeting"""

> fastparse.parse(input, Parser.expr(using _))
res15: fastparse.Parsed[Expr] = Success(
  value = Local(
    name = "greeting",
    assigned = Str("Hello "),
    body = Plus(left = Ident("greeting"), right = Ident("greeting"))
  ),
  index = 46
)
