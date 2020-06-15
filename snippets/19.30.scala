@ {
  def expr[_: P] = P( "(" ~ parser ~ ")" | number )
  def parser[_: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr )
  }
cmd80.sc:2: type mismatch;
 found   : fastparse.core.Parser[(Int, String, Int)]
 required: fastparse.all.Parser[Expr]
    (which expands to)  fastparse.core.Parser[Expr]
val parser: Parser[Int] = P( expr ~ ws ~ operator ~ ws ~ expr )
                                                       ^
Compilation Failed
