> def ident[T: P] = P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!.map(Expr.Ident(_))

> fastparse.parse("hello", ident(using _))
res4: fastparse.Parsed[Expr.Ident] = Success(
  value = Ident("hello"),
  index = 5
)

> fastparse.parse("123", ident(using _)) // Identifiers cannot start with a number
res5: fastparse.Parsed[Expr.Ident] = Parsed.Failure(Position 1:1, found "123")
