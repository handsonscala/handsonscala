Expr.Func(
  List("a", "b"),
  Expr.Plus(
    Expr.Plus(
      Expr.Ident("a"),
      Expr.Str(" ")
    ),
    Expr.Ident("b")
  )
)