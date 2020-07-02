@ {
  def expr[_: P]: P[Expr] = P( prefixExpr ~ plus.rep ).map{
    case (left, rights) => rights.foldLeft(left)(Expr.Plus(_, _))
  }
  def plus[_: P] = P( "+" ~ prefixExpr )
  def prefixExpr[_: P] = P( str | ident )
  def str[_: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" ).map(Expr.Str)
  def ident[_: P] =
    P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!.map(Expr.Ident)
  }
