//| mvnDeps:
//| - com.lihaoyi::fastparse:3.1.1
import fastparse.*, NoWhitespace.*

enum Expr:
  case BinOp(left: Expr, op: String, right: Expr)
  case Number(value: Int)

def number[T: P] = P(
  "zero" | "one" | "two" | "three" | "four" |
    "five" | "six" | "seven" | "eight" | "nine"
).!.map:
  case "zero"  => Expr.Number(0); case "one"   => Expr.Number(1)
  case "two"   => Expr.Number(2); case "three" => Expr.Number(3)
  case "four"  => Expr.Number(4); case "five"  => Expr.Number(5)
  case "six"   => Expr.Number(6); case "seven" => Expr.Number(7)
  case "eight" => Expr.Number(8); case "nine"  => Expr.Number(9)

def ws[T: P] = P( " ".rep(1) )
def operator[T: P] = P( "plus" | "minus" | "times" | "divide" ).!
def expr[T: P] = P( "(" ~ parser ~ ")" | number )

val precedence = Map("plus" -> 1, "minus" -> 1, "times" -> 2, "divide" -> 2)

def climb(left: Expr,
          rights: collection.mutable.ArrayDeque[(String, Expr)],
          minPrec: Int): Expr =
  var result = left
  var done = false
  while !done do
    rights.headOption match
      case None => done = true
      case Some((op, next)) =>
        val prec: Int = precedence(op)
        if prec < minPrec then done = true
        else
          rights.removeHead()
          val rhs = climb(next, rights, prec + 1)
          result = Expr.BinOp(result, op, rhs)
  result

def parser[T: P]: P[Expr] = P( expr ~ (ws ~ operator ~ ws ~ expr).rep ).map:
  case (lhs, rights) => climb(lhs, rights.to(collection.mutable.ArrayDeque), 1)
