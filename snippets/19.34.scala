import fastparse._, NoWhitespace._

sealed trait Expr
case class BinOp(left: Expr, op: String, right: Expr) extends Expr
case class Number(Value: Int) extends Expr
def number[_: P] = P(
  "zero" | "one" | "two" | "three" | "four" |
  "five" | "six" | "seven" | "eight" | "nine"
).!.map{
  case "zero"  => Number(0); case "one"   => Number(1)
  case "two"   => Number(2); case "three" => Number(3)
  case "four"  => Number(4); case "five"  => Number(5)
  case "six"   => Number(6); case "seven" => Number(7)
  case "eight" => Number(8); case "nine"  => Number(9)
}
def ws[_: P] = P( " ".rep(1) )
def operator[_: P] = P( "plus" | "minus" | "times" | "divide" ).!
def expr[_: P] = P( "(" ~ parser ~ ")" | number )
def parser[_: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr ).map{
  case (lhs, op, rhs) => BinOp(lhs, op, rhs)
}