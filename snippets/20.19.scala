> def str[T: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" ).map(Expr.Str(_))

> fastparse.parse("\"hello\"", str(using _))
res0: fastparse.Parsed[Expr.Str] = Success(value = Str("hello"), index = 7)

> fastparse.parse("\"hello world\"", str(using _))
res1: fastparse.Parsed[Expr.Str] = Success(
  value = Str("hello world"),
  index = 13
)

> fastparse.parse("\"\"", str(using _))
res2: fastparse.Parsed[Expr.Str] = Success(value = Str(""), index = 2)

> fastparse.parse("123", str(using _))
res3: fastparse.Parsed[Expr.Str] = Parsed.Failure(Position 1:1, found "123")
