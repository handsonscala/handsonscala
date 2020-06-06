def expr[_: P]: P[Expr] = P( "(" ~ parser ~ ")" | number )
def parser[_: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr )
