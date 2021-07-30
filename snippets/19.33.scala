@ def stringify(e: Expr): String = e match {
    case BinOp(left, op, right) => s"(${stringify(left)} $op ${stringify(right)})"
    case Number(0) => "zero";   case Number(1) => "one"
    case Number(2) => "two";    case Number(3) => "three"
    case Number(4) => "four";   case Number(5) => "five"
    case Number(6) => "six";    case Number(7) => "seven"
    case Number(8) => "eight";  case Number(9) => "nine"
  }

@ stringify(t)
res59: String = "((one plus two) times (three plus four))"

@ def evaluate(e: Expr): Int = e match {
    case BinOp(left, "plus", right) => evaluate(left) + evaluate(right)
    case BinOp(left, "minus", right) => evaluate(left) - evaluate(right)
    case BinOp(left, "times", right) => evaluate(left) * evaluate(right)
    case BinOp(left, "divide", right) => evaluate(left) / evaluate(right)
    case Number(n) => n
  }

@ evaluate(t)
res61: Int = 10
