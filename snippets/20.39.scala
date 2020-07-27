 def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match {
   ...
   case Expr.Plus(left, right) => ...
+  case Expr.Local(name, assigned, body) =>
+    val assignedValue = evaluate(assigned, scope)
+    evaluate(body, scope + (name -> assignedValue))
 }
