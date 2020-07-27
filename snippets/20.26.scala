@ fastparse.parse("""{"a": "b", "cde": id, "nested": {}}""", expr(_))
res15: Parsed[Expr] = Success(
  Dict(Map("a" -> Str("b"), "cde" -> Ident("id"), "nested" -> Dict(Map()))),
  35
)
