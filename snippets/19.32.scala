> val t = fastparse.parse(
    "(one plus two) times (three plus four)",
    parser(using _)
  ).get.value
t: Expr = BinOp(
  left = BinOp(left = Number(1), op = "plus", right = Number(2)),
  op = "times",
  right = BinOp(left = Number(3), op = "plus", right = Number(4))
)
