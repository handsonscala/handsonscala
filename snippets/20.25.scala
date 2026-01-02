  def expr[T: P]: P[Expr] = P( prefixExpr ~ plus.rep ).map:
    case (left, items) => items.foldLeft(left)(Expr.Plus(_, _))

  def plus[T: P] = P( "+" ~ prefixExpr )
- def prefixExpr[T: P] = P( str | ident )
+ def prefixExpr[T: P] = P( str | ident | dict )
+ def dict[T: P] = P( "{" ~/ (str0 ~ ":" ~/ expr).rep(0, ",") ~ "}" )
+   .map(kvs => Expr.Dict(kvs.toMap))
+
- def str[T: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
-   .map(Expr.Str(_))
-
+ def str[T: P] = P( str0 ).map(Expr.Str(_))
+ def str0[T: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
  def ident[T: P] = P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!
    .map(Expr.Ident(_))
