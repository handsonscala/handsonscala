> evaluate(
    fastparse.parse("\"hello\" + \"world\"", Parser.expr(using _)).get.value,
    Map.empty
  )
res14: Value = Str("helloworld")
