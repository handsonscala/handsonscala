def expr[T: P]: P[Expr] = P( "(" ~ parser ~ ")" | number )
def parser[T: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr )
