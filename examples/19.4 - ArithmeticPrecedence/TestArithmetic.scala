//| moduleDeps: [Arithmetic.scala, Traversals.scala]

def main() =
  val t = fastparse.parse("one plus two times three plus four", parser(using _)).get.value

  pprint.log(stringify(t))
  assert(stringify(t) == "((one plus (two times three)) plus four)")
  pprint.log(evaluate(t))
  assert(evaluate(t) == 11)
