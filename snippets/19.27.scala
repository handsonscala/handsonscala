> def number[T: P]: P[Expr] = P(
    "zero" | "one" | "two" | "three" | "four" |
    "five" | "six" | "seven" | "eight" | "nine"
  ).!.map:
    case "zero"  => Expr.Number(0); case "one"   => Expr.Number(1)
    case "two"   => Expr.Number(2); case "three" => Expr.Number(3)
    case "four"  => Expr.Number(4); case "five"  => Expr.Number(5)
    case "six"   => Expr.Number(6); case "seven" => Expr.Number(7)
    case "eight" => Expr.Number(8); case "nine"  => Expr.Number(9)
