@ val input = """local thing = "kay"; {"f": function(a) a + a, "nested": {"k": v}}"""

@ fastparse.parse(input, Parser.expr(_))
res21: Parsed[Expr] = Success(
  Local(
    "thing",
    Str("kay"),
    Dict(
      Map(
        "f" -> Func(List("a"), Plus(Ident("a"), Ident("a"))),
        "nested" -> Dict(Map("k" -> Ident("v")))
      )
    )
  ),
  65
)
