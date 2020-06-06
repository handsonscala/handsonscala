
sealed trait Expr
case class BinOp(left: Expr, op: String, right: Expr) extends Expr
case class Literal(value: Int) extends Expr
case class Variable(name: String) extends Expr

def stringify(expr: Expr): String = expr match {
  case BinOp(left, op, right) => s"(${stringify(left)} $op ${stringify(right)})"
  case Literal(value) => value.toString
  case Variable(name) => name
}

def simplify(expr: Expr): Expr = {
  val res = expr match{
    case BinOp(Literal(left), "+", Literal(right)) => Literal(left + right)
    case BinOp(Literal(left), "-", Literal(right)) => Literal(left - right)
    case BinOp(Literal(left), "*", Literal(right)) => Literal(left * right)

    case BinOp(left, "*", Literal(1)) => simplify(left)
    case BinOp(Literal(1), "*", right) => simplify(right)

    case BinOp(left, "+", Literal(0)) => simplify(left)
    case BinOp(Literal(0), "+", right) => simplify(right)

    case BinOp(left, "-", Literal(0)) => simplify(left)
    case BinOp(Literal(0), "-", right) => simplify(right)

    case BinOp(left, "*", Literal(0)) => Literal(0)
    case BinOp(Literal(0), "*", right) => Literal(0)

    case BinOp(left, "+", right) => BinOp(simplify(left), "+", simplify(right))
    case BinOp(left, "-", right) => BinOp(simplify(left), "-", simplify(right))
    case BinOp(left, "*", right) => BinOp(simplify(left), "*", simplify(right))

    case Literal(value) => Literal(value)
    case Variable(name) => Variable(name)
  }

  // We may need to re-simplify an expression multiple times in order to achieve
  // all the simplifications we want; only stop re-simplifying it if it stops changing
  if (res == expr) res
  else simplify(res)
}

val example1 = BinOp(Literal(1), "+", Literal(1))

val str1 = pprint.log(stringify(example1))
val simple1 = pprint.log(stringify(simplify(example1)))
assert(str1 == "(1 + 1)")
assert(simple1 == "2")

val example2 = BinOp(BinOp(Literal(1), "+", Literal(1)), "*", Variable("x"))

val str2 = pprint.log(stringify(example2))
val simple2 = pprint.log(stringify(simplify(example2)))
assert(str2 == "((1 + 1) * x)")
assert(simple2 == "(2 * x)")

val example3 = BinOp(
  BinOp(Literal(2), "-", Literal(1)),
  "*",
  Variable("x")
)

val str3 = pprint.log(stringify(example3))
val simple3 = pprint.log(stringify(simplify(example3)))
assert(str3 == "((2 - 1) * x)")
assert(simple3 == "x")

val example4 = BinOp(
  BinOp(BinOp(Literal(1), "+", (Literal(1))), "*", Variable("y")),
  "+",
  BinOp(BinOp(Literal(1), "-", (Literal(1))), "*", Variable("x"))
)

val str4 = pprint.log(stringify(example4))
val simple4 = pprint.log(stringify(simplify(example4)))
assert(str4 == "(((1 + 1) * y) + ((1 - 1) * x))")
assert(simple4 == "(2 * y)")
