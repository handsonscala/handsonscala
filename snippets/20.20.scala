@ def ident[_: P] =
    P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!.map(Expr.Ident)

@ fastparse.parse("hello", ident(_))
res8: Parsed[Expr.Ident] = Success(Ident("hello"), 5)

@ fastparse.parse("123", ident(_)) // Identifiers cannot start with a number
res9: Parsed[Expr.Ident] = Parsed.Failure(Position 1:1, found "123")
