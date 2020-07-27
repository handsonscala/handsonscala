 def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match {
   case Expr.Str(s) => Value.Str(s)
+  case Expr.Dict(kvs) => Value.Dict(kvs.map{case (k, v) => (k, evaluate(v, scope))})
 }
