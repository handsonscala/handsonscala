@ val t = fastparse.parse("(one plus two) times (three plus four)", parser(_)).get.value
t: Parsed[Expr] = Success(
  BinOp(
    BinOp(Number(1), "plus", Number(2)),
    "times",
    BinOp(Number(3), "plus", Number(4))
  ),
  38
)
