def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match
  case Expr.Str(s) => Value.Str(s)
  case Expr.Dict(kvs) => Value.Dict(kvs.map((k, v) => (k, evaluate(v, scope))))
  case Expr.Plus(left, right) =>
    val Value.Str(leftStr) = evaluate(left, scope).runtimeChecked
    val Value.Str(rightStr) = evaluate(right, scope).runtimeChecked
    Value.Str(leftStr + rightStr)
