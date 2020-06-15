@ def jsonnet(input: String): String = {
    serialize(evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty))
  }
