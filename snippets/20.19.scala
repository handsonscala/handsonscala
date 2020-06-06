@ def str[_: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" ).map(Expr.Str)

@ fastparse.parse("\"hello\"", str(_))
res3: Parsed[Expr.Str] = Success(Str("hello"), 7)

@ fastparse.parse("\"hello world\"", str(_))
res4: Parsed[Expr.Str] = Success(Str("hello world"), 13)

@ fastparse.parse("\"\"", str(_))
res5: Parsed[Expr.Str] = Success(Str(""), 2)

@ fastparse.parse("123", str(_))
res6: Parsed[Expr.Str] = Parsed.Failure(Position 1:1, found "123")
