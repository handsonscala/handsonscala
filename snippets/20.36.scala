 def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match {
   ...
   case Expr.Dict(kvs) => ...
+  case Expr.Plus(left, right) =>
+    val Value.Str(leftStr) = evaluate(left, scope)
+    val Value.Str(rightStr) = evaluate(right, scope)
+    Value.Str(leftStr + rightStr)
 }
