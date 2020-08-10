import $file.Arithmetic, Arithmetic._
import $file.Traversals, Traversals._

val t = fastparse.parse("one plus two times three plus four", parser(_)).get.value

pprint.log(stringify(t))
assert(stringify(t) == "((one plus (two times three)) plus four)")
pprint.log(evaluate(t))
assert(evaluate(t) == 11)
