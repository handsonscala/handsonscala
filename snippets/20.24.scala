> fastparse.parse("a + b", expr(using _))
res6: fastparse.Parsed[Expr] = Success(
  value = Plus(left = Ident("a"), right = Ident("b")),
  index = 5
)

> fastparse.parse("""a + " " + c""", expr(using _))
res7: fastparse.Parsed[Expr] = Success(
  value = Plus(
    left = Plus(left = Ident("a"), right = Str(" ")),
    right = Ident("c")
  ),
  index = 11
)
