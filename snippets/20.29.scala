> fastparse.parse("""{"a": "A", "b": "bee"}""", Parser.expr(using _))
res9: fastparse.Parsed[Expr] = Success(
  value = Dict(Map("a" -> Str("A"), "b" -> Str("bee"))),
  index = 22
)
