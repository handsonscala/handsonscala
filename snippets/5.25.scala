@ val smallExpr = BinOp(
    Variable("x"),
    "+",
    Literal(1)
  )

@ stringify(smallExpr)
res52: String = "(x + 1)"
