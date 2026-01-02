> {
  def expr[T: P]: P[Expr] = P( prefixExpr ~ plus.rep ).map:
    case (left, rights) => rights.foldLeft(left)(Expr.Plus(_, _))
  def plus[T: P] = P( "+" ~ prefixExpr )
  def prefixExpr[T: P] = P( str | ident )
  def str[T: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
    .map(Expr.Str(_))
  def ident[T: P] = P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!
    .map(Expr.Ident(_))
  }
