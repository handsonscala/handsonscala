@ def number[_: P]: P[Expr] = P(
    "zero" | "one" | "two" | "three" | "four" |
    "five" | "six" | "seven" | "eight" | "nine"
  ).!.map{
    case "zero"  => Number(0); case "one"   => Number(1)
    case "two"   => Number(2); case "three" => Number(3)
    case "four"  => Number(4); case "five"  => Number(5)
    case "six"   => Number(6); case "seven" => Number(7)
    case "eight" => Number(8); case "nine"  => Number(9)
  }
