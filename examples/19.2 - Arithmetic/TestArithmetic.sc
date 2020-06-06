import $file.Arithmetic, Arithmetic._
import $file.Traversals, Traversals._

val t = fastparse.parse("(one plus two) times (three plus four)", parser(_)).get.value

assert(stringify(t) == "((one plus two) times (three plus four))")
assert(evaluate(t) == 21)
