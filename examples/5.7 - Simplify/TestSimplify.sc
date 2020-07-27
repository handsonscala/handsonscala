import $file.Simplify, Simplify._
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
