//| moduleDeps: [Arithmetic.scala, Traversals.scala]

def main() =
  val t = fastparse.parse("(one plus two) times (three plus four)", parser(using _)).get.value

  assert(stringify(t) == "((one plus two) times (three plus four))")
  assert(evaluate(t) == 21)
