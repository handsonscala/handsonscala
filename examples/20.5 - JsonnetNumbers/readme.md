# Example 20.5 - JsonnetNumbers
Jsonnet interpreter with added support for simple integer arithmetic

```bash
./mill -i TestJsonnet.scala
```

## Upstream Example: [20.4 - Jsonnet](https://github.com/handsonscala/handsonscala/tree/v2/examples/20.4%20-%20Jsonnet):
Diff:
```diff
diff --git a/20.4 - Jsonnet/Jsonnet.scala b/20.5 - JsonnetNumbers/Jsonnet.scala
index 0488512..ee34992 100644
--- a/20.4 - Jsonnet/Jsonnet.scala	
+++ b/20.5 - JsonnetNumbers/Jsonnet.scala	
@@ -2,6 +2,7 @@
 //| - com.lihaoyi::fastparse:3.1.1
 enum Expr:
   case Str(s: String)
+  case Num(i: Int)
   case Ident(name: String)
   case Plus(left: Expr, right: Expr)
   case Dict(pairs: Map[String, Expr])
@@ -17,9 +18,10 @@ object Parser:
   def prefixExpr[T: P]: P[Expr] = P( callExpr ~ call.rep ).map:
     case (e, items) => items.foldLeft(e)(Expr.Call(_, _))
   
-  def callExpr[T: P] = P( str | dict | local | func | ident )
+  def callExpr[T: P] = P( num | str | dict | local | func | ident )
 
   def str[T: P] = P( str0 ).map(Expr.Str(_))
+  def num[T: P] = P( CharsWhileIn("0-9").! ).map(s => Expr.Num(s.toInt))
   def str0[T: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
   def ident[T: P] = P( ident0 ).map(Expr.Ident(_))
   def ident0[T: P] = P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!
@@ -35,16 +37,18 @@ object Parser:
 
 enum Value:
   case Str(s: String)
+  case Num(i: Int)
   case Dict(pairs: Map[String, Value])
   case Func(call: Seq[Value] => Value)
 
 def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match
   case Expr.Str(s) => Value.Str(s)
+  case Expr.Num(i) => Value.Num(i)
   case Expr.Dict(kvs) => Value.Dict(kvs.map((k, v) => (k, evaluate(v, scope))))
   case Expr.Plus(left, right) =>
-    val Value.Str(leftStr) = evaluate(left, scope).runtimeChecked
-    val Value.Str(rightStr) = evaluate(right, scope).runtimeChecked
-    Value.Str(leftStr + rightStr)
+    (evaluate(left, scope), evaluate(right, scope)).runtimeChecked match
+      case (Value.Str(leftStr), Value.Str(rightStr)) => Value.Str(leftStr + rightStr)
+      case (Value.Num(leftNum), Value.Num(rightNum)) => Value.Num(leftNum + rightNum)
   
   case Expr.Local(name, assigned, body) =>
     val assignedValue = evaluate(assigned, scope)
@@ -61,6 +65,7 @@ def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match
 
 def serialize(v: Value): String = v.runtimeChecked match
   case Value.Str(s) => "\"" + s + "\""
+  case Value.Num(i) => i.toString
   case Value.Dict(kvs) =>
     kvs.map((k, v) => "\"" + k + "\": " + serialize(v)).mkString("{", ", ", "}")
 
diff --git a/20.4 - Jsonnet/TestJsonnet.scala b/20.5 - JsonnetNumbers/TestJsonnet.scala
index 11fb832..7341a44 100644
--- a/20.4 - Jsonnet/TestJsonnet.scala	
+++ b/20.5 - JsonnetNumbers/TestJsonnet.scala	
@@ -1,18 +1,20 @@
 //| moduleDeps: [Jsonnet.scala]
 
-def main(): Unit =
+def main() =
   assert(
     pprint.log(jsonnet(
       """local greeting = "Hello ";
-         local person = function (name) {
+         local bonus = 15000;
+         local person = function (name, baseSalary) {
            "name": name,
-           "welcome": greeting + name + "!"
+           "welcome": greeting + name + "!",
+           "totalSalary": baseSalary + bonus
          };
          {
-           "person1": person("Alice"),
-           "person2": person("Bob"),
-           "person3": person("Charlie")
+           "person1": person("Alice", 50000),
+           "person2": person("Bob", 60000),
+           "person3": person("Charlie", 70000)
          }"""
     )) ==
-    """{"person1": {"name": "Alice", "welcome": "Hello Alice!"}, "person2": {"name": "Bob", "welcome": "Hello Bob!"}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!"}}"""
+    """{"person1": {"name": "Alice", "welcome": "Hello Alice!", "totalSalary": 65000}, "person2": {"name": "Bob", "welcome": "Hello Bob!", "totalSalary": 75000}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!", "totalSalary": 85000}}"""
   )
```
