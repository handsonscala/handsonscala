> {
  def expr[T: P] = P( "(" ~ parser ~ ")" | number )
  def parser[T: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr )
  }
-- [E172] Type Error: ----------------------------------------------------------
3 |def parser[T: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr )
  |                                                               ^
  |No given instance of type fastparse.Implicits.Sequencer[(Expr, String), Expr,
  |Expr] was found for parameter s of method ~ in package fastparse
