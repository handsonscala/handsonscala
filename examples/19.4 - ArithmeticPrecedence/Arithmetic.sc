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

val precedence = Map("plus" -> 1, "minus" -> 1, "times" -> 2, "divide" -> 2)

def climb(left: Expr,
          rights: collection.mutable.ArrayDeque[(String, Expr)],
          minPrec: Int): Expr = {
  var result = left
  var done = false
  while(!done) {
    rights.headOption match {
      case None => done = true
      case Some((op, next)) =>
        val prec: Int = precedence(op)
        if (prec < minPrec) done = true
        else{
          rights.removeHead()
          val rhs = climb(next, rights, prec + 1)
          result = BinOp(result, op, rhs)
        }
    }
  }
  result
}

def parser[_: P]: P[Expr] = P( expr ~ (ws ~ operator ~ ws ~ expr).rep ).map{
  case (lhs, rights) => climb(lhs, rights.to(collection.mutable.ArrayDeque), 1)
}
