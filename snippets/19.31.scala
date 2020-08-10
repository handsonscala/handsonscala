@ {
  def expr[_: P] = P( "(" ~ parser ~ ")" | number )
  def parser[_: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr ).map{
    case (lhs, op, rhs) => BinOp(lhs, op, rhs)
  }
  }
