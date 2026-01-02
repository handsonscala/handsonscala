//| mvnDeps:
//| - com.lihaoyi::fastparse:3.1.1
import fastparse.*, NoWhitespace.*

def number[T: P] = P(
  "zero" | "one" | "two" | "three" | "four" |
    "five" | "six" | "seven" | "eight" | "nine"
).!.map:
  case "zero"  => 0; case "one"   => 1; case "two" => 2; case "three" => 3
  case "four"  => 4; case "five"  => 5; case "six" => 6; case "seven" => 7
  case "eight" => 8; case "nine"  => 9

def ws[T: P] = P( " ".rep(1) )
def operator[T: P] = P( "plus" | "minus" | "times" | "divide" ).!
def expr[T: P] = P( "(" ~ parser ~ ")" | number )
def parser[T: P]: P[Int] = P( expr ~ ws ~ operator ~ ws ~ expr ).map:
  case (lhs, "plus", rhs) => lhs + rhs
  case (lhs, "minus", rhs) => lhs - rhs
  case (lhs, "times", rhs) => lhs * rhs
  case (lhs, "divide", rhs) => lhs / rhs
