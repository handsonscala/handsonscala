@ val input = """local greeting = "Hello "; greeting + greeting"""

@ fastparse.parse(input, Parser.expr(_))
res30: Parsed[Expr] = Success(
  Local("greeting", Str("Hello "), Plus(Ident("greeting"), Ident("greeting"))),
  45
)
