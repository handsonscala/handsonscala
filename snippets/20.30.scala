> fastparse.parse("""f()(a) + g(b, c)""", Parser.expr(using _))
res10: fastparse.Parsed[Expr] = Success(
  value = Plus(
    left = Call(
      expr = Call(expr = Ident("f"), args = List()),
      args = List(Ident("a"))
    ),
    right = Call(expr = Ident("g"), args = List(Ident("b"), Ident("c")))
  ),
  index = 16
)
