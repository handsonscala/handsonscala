@ fastparse.parse("""{"a": "A", "b": "bee"}""", Parser.expr(_))
res17: Parsed[Expr] = Success(Dict(Map("a" -> Str("A"), "b" -> Str("bee"))), 22)

@ fastparse.parse("""f()(a) + g(b, c)""", Parser.expr(_))
res18: Parsed[Expr] = Success(
  Plus(
    Call(Call(Ident("f"), List()), List(Ident("a"))),
    Call(Ident("g"), List(Ident("b"), Ident("c")))
  ),
  16
)
