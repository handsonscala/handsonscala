enum Expr:
  case Str(s: String)
  case Ident(name: String)
  case Plus(left: Expr, right: Expr)
  case Dict(pairs: Map[String, Expr])
  case Local(name: String, assigned: Expr, body: Expr)
  case Func(argNames: Seq[String], body: Expr)
  case Call(expr: Expr, args: Seq[Expr])
