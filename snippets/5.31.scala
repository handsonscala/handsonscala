> def eval(expr: Expr, values: Map[String, Int]): Int =
    expr.runtimeChecked match
      case BinOp(left, "+", right) => eval(left, values) + eval(right, values)
      case BinOp(left, "-", right) => eval(left, values) - eval(right, values)
      case BinOp(left, "*", right) => eval(left, values) * eval(right, values)
      case Literal(value) => value
      case Variable(name) => values(name)

> eval(largeExpr, Map("x" -> 10, "y" -> 20))
res17: Int = 209
