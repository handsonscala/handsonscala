> val input = """local thing = "kay"; {"f": function(a) a + a, "nested": {"k": v}}"""

> fastparse.parse(input, Parser.expr(using _))
res11: fastparse.Parsed[Expr] = Success(
  value = Local(
    name = "thing",
    assigned = Str("kay"),
    body = Dict(
      Map(
        "f" -> Func(
          argNames = List("a"),
          body = Plus(left = Ident("a"), right = Ident("a"))
        ),
        "nested" -> Dict(Map("k" -> Ident("v")))
      )
    )
  ),
  index = 65
)
