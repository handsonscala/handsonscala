//| moduleDeps: [Parse.scala, Values.scala]
def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match
  case Expr.Str(s) => Value.Str(s)
  case Expr.Dict(kvs) => Value.Dict(kvs.map((k, v) => (k, evaluate(v, scope))))
  case Expr.Plus(left, right) =>
    val Value.Str(leftStr) = evaluate(left, scope).runtimeChecked
    val Value.Str(rightStr) = evaluate(right, scope).runtimeChecked
    Value.Str(leftStr + rightStr)
  
  case Expr.Local(name, assigned, body) =>
    val assignedValue = evaluate(assigned, scope)
    evaluate(body, scope + (name -> assignedValue))
  
  case Expr.Ident(name) => scope(name)
  case Expr.Call(expr, args) =>
    val Value.Func(call) = evaluate(expr, scope).runtimeChecked
    val evaluatedArgs = args.map(evaluate(_, scope))
    call(evaluatedArgs)
  
  case Expr.Func(argNames, body) =>
    Value.Func(args => evaluate(body, scope ++ argNames.zip(args)))
