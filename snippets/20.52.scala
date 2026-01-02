> def jsonnet(input: String): String =
    serialize(evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty))
