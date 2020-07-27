@ fastparse.parse("a + b", expr(_))
res12: Parsed[Expr] = Success(Plus(Ident("a"), Ident("b")), 5)

@ fastparse.parse("""a + " " + c""", expr(_))
res13: Parsed[Expr] = Success(Plus(Plus(Ident("a"), Str(" ")), Ident("c")), 11)
