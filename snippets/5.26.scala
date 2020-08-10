@ val largeExpr = BinOp(
    BinOp(Variable("x"), "+", Literal(1)),
    "*",
    BinOp(Variable("y"), "-", Literal(1))
  )

@ stringify(largeExpr)
res54: String = "((x + 1) * (y - 1))"
