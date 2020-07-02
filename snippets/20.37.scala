@ evaluate(fastparse.parse("\"hello\" + \"world\"", Parser.expr(_)).get.value, Map.empty)
res28: Value = Str("helloworld")
