> {
  def expr[T: P] = P( "(" ~ parser ~ ")" | number )
  def parser[T: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr ).map:
    case (lhs, op, rhs) => Expr.BinOp(lhs, op, rhs)
  }
