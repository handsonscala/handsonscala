 def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match
   ...
   case Expr.Dict(kvs) => ...
+  case Expr.Plus(left, right) =>
+    val Value.Str(leftStr) = evaluate(left, scope).runtimeChecked
+    val Value.Str(rightStr) = evaluate(right, scope).runtimeChecked
+    Value.Str(leftStr + rightStr)
