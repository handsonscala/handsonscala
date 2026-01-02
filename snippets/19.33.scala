> def stringify(e: Expr): String = e match
    case Expr.BinOp(left, op, right) => s"(${stringify(left)} $op ${stringify(right)})"
    case Expr.Number(0) => "zero";   case Expr.Number(1) => "one"
    case Expr.Number(2) => "two";    case Expr.Number(3) => "three"
    case Expr.Number(4) => "four";   case Expr.Number(5) => "five"
    case Expr.Number(6) => "six";    case Expr.Number(7) => "seven"
    case Expr.Number(8) => "eight";  case Expr.Number(9) => "nine"

> stringify(t)
res40: String = "((one plus two) times (three plus four))"

> def evaluate(e: Expr): Int = e match
    case Expr.BinOp(left, "plus", right) => evaluate(left) + evaluate(right)
    case Expr.BinOp(left, "minus", right) => evaluate(left) - evaluate(right)
    case Expr.BinOp(left, "times", right) => evaluate(left) * evaluate(right)
    case Expr.BinOp(left, "divide", right) => evaluate(left) / evaluate(right)
    case Expr.Number(n) => n

> evaluate(t)
res41: Int = 21
