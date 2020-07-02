@ def evaluate(expr: Expr, values: Map[String, Int]): Int = expr match {
    case BinOp(left, "+", right) => evaluate(left, values) + evaluate(right, values)
    case BinOp(left, "-", right) => evaluate(left, values) - evaluate(right, values)
    case BinOp(left, "*", right) => evaluate(left, values) * evaluate(right, values)
    case Literal(value) => value
    case Variable(name) => values(name)
  }

@ evaluate(smallExpr, Map("x" -> 10))
res56: Int = 11

@ evaluate(largeExpr, Map("x" -> 10, "y" -> 20))
res57: Int = 209
