def dayOfWeek(x: Int) = x match {
  case 1 => "Mon"; case 2 => "Tue"
  case 3 => "Wed"; case 4 => "Thu"
  case 5 => "Fri"; case 6 => "Sat"
  case 7 => "Sun"; case _ => "Unknown"
}

assert(dayOfWeek(5) == "Fri")

assert(dayOfWeek(-1) == "Unknown")

def indexOfDay(d: String) = d match {
  case "Mon" => 1; case "Tue" => 2
  case "Wed" => 3; case "Thu" => 4
  case "Fri" => 5; case "Sat" => 6
  case "Sun" => 7; case _ => -1
}

assert(indexOfDay("Fri") == 5)

assert(indexOfDay("???") == -1)

val fizzBuzz1 = for (i <- Range.inclusive(1, 100)) yield {
  val s = (i % 3, i % 5) match {
    case (0, 0) => "FizzBuzz"
    case (0, _) => "Fizz"
    case (_, 0) => "Buzz"
    case _ => i
  }
  s
}

assert(fizzBuzz1.take(5) == Seq(1, 2, "Fizz", 4, "Buzz"))

val fizzBuzz2 = for (i <- Range.inclusive(1, 100)) yield {
  val s = (i % 3 == 0, i % 5 == 0) match {
    case (true, true) => "FizzBuzz"
    case (true, false) => "Fizz"
    case (false, true) => "Buzz"
    case (false, false) => i
  }
  s
}

assert(fizzBuzz2.take(5) == Seq(1, 2, "Fizz", 4, "Buzz"))

case class Point(x: Int, y: Int)

def direction(p: Point) = p match {
  case Point(0, 0) => "origin"
  case Point(_, 0) => "horizontal"
  case Point(0, _) => "vertical"
  case _ => "diagonal"
}

assert(direction(Point(0, 0)) == "origin")

assert(direction(Point(1, 1)) == "diagonal")

assert(direction(Point(10, 0)) == "horizontal")

def splitDate(s: String) = s match {
  case s"$day-$month-$year" =>
    s"day: $day, mon: $month, yr: $year"
  case _ => "not a date"
}

assert(splitDate("9-8-1965") == "day: 9, mon: 8, yr: 1965")

assert(splitDate("9-8") == "not a date")

case class Person(name: String, title: String)

def greet(p: Person) = p match {
  case Person(s"$firstName $lastName", title) => s"Hello $title $lastName"
  case Person(name, title) => s"Hello $title $name"
}

assert(greet(Person("Haoyi Li", "Mr")) == "Hello Mr Li")
assert(greet(Person("Who?", "Dr")) == "Hello Dr Who?")

def greet2(husband: Person, wife: Person) = (husband, wife) match {
  case (Person(s"$first1 $last1", _), Person(s"$first2 $last2", _)) if last1 == last2 =>
    s"Hello Mr and Ms $last1"

  case (Person(name1, _), Person(name2, _)) => s"Hello $name1 and $name2"
}

assert(greet2(Person("James Bond", "Mr"), Person("Jane Bond", "Ms")) == "Hello Mr and Ms Bond")
assert(greet2(Person("James Bond", "Mr"), Person("Jane", "Ms")) == "Hello James Bond and Jane")

val a = Array[(Int, String)]((1, "one"), (2, "two"), (3, "three"))

val aResult = for ((i, s) <- a) yield s + i

assert(aResult.toSeq == Seq("one1", "two2", "three3"))

val p = Point(123, 456)

val Point(x, y) = p
assert(x == 123)
assert(y == 456)

val s"$first $second" = "Hello World"

assert(first == "Hello")
assert(second == "World")
val flipped = s"$second $first"

assert(flipped == "World Hello")

sealed trait Expr
case class BinOp(left: Expr, op: String, right: Expr) extends Expr
case class Literal(value: Int) extends Expr
case class Variable(name: String) extends Expr

def stringify(expr: Expr): String = expr match {
  case BinOp(left, op, right) => s"(${stringify(left)} $op ${stringify(right)})"
  case Literal(value) => value.toString
  case Variable(name) => name
}

val smallExpr = BinOp(
  Variable("x"),
  "+",
  Literal(1)
)

assert(stringify(smallExpr) == "(x + 1)")

val largeExpr = BinOp(
  BinOp(Variable("x"), "+", Literal(1)),
  "*",
  BinOp(Variable("y"), "-", Literal(1))
)
assert(stringify(largeExpr) == "((x + 1) * (y - 1))")

def evaluate(expr: Expr, values: Map[String, Int]): Int = expr match {
  case BinOp(left, "+", right) => evaluate(left, values) + evaluate(right, values)
  case BinOp(left, "-", right) => evaluate(left, values) - evaluate(right, values)
  case BinOp(left, "*", right) => evaluate(left, values) * evaluate(right, values)
  case Literal(value) => value
  case Variable(name) => values(name)
}

assert(evaluate(smallExpr, Map("x" -> 10)) == 11)
assert(evaluate(largeExpr, Map("x" -> 10, "y" -> 20)) == 209)
