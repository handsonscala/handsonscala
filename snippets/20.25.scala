  def expr[_: P]: P[Expr] = P( prefixExpr ~ plus.rep ).map{
    case (left, items) => items.foldLeft(left)(Expr.Plus(_, _))
  }
  def plus[_: P] = P( "+" ~ prefixExpr )
- def prefixExpr[_: P] = P( str | ident )
+ def prefixExpr[_: P] = P( str | ident | dict )
+ def dict[_: P] =
+   P( "{" ~/ (str0 ~ ":" ~/ expr).rep(0, ",") ~ "}" ).map(kvs => Expr.Dict(kvs.toMap))
- def str[_: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" ).map(Expr.Str)
+ def str[_: P] = P( str0 ).map(Expr.Str)
+ def str0[_: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
  def ident[_: P] =
    P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!.map(Expr.Ident)
