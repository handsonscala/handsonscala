# Example 20.5 - JsonnetNumbers
Jsonnet interpreter with added support for simple integer arithmetic

```bash
amm TestJsonnet.sc
```

## Upstream Example: [20.4 - Jsonnet](https://github.com/handsonscala/handsonscala/tree/v1/examples/20.4%20-%20Jsonnet):
Diff:
```diff
diff --git a/20.4 - Jsonnet/Jsonnet.sc b/20.5 - JsonnetNumbers/Jsonnet.sc
index 24df803..10e6891 100644
--- a/20.4 - Jsonnet/Jsonnet.sc	
+++ b/20.5 - JsonnetNumbers/Jsonnet.sc	
@@ -1,6 +1,7 @@
 sealed trait Expr
 object Expr{
   case class Str(s: String) extends Expr
+  case class Num(i: Int) extends Expr
   case class Ident(name: String) extends Expr
   case class Plus(left: Expr, right: Expr) extends Expr
   case class Dict(pairs: Map[String, Expr]) extends Expr
@@ -16,9 +17,10 @@ object Parser {
   def prefixExpr[_: P]: P[Expr] = P( callExpr ~ call.rep ).map{
     case (e, items) => items.foldLeft(e)(Expr.Call(_, _))
   }
-  def callExpr[_: P] = P( str | dict | local | func | ident )
+  def callExpr[_: P] = P( num | str | dict | local | func | ident )
 
   def str[_: P] = P( str0 ).map(Expr.Str)
+  def num[_: P] = P( CharsWhileIn("0-9").! ).map(s => Expr.Num(s.toInt))
   def str0[_: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
   def ident[_: P] = P( ident0 ).map(Expr.Ident)
   def ident0[_: P] = P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!
@@ -36,16 +38,19 @@ object Parser {
 sealed trait Value
 object Value{
   case class Str(s: String) extends Value
+  case class Num(i: Int) extends Value
   case class Dict(pairs: Map[String, Value]) extends Value
   case class Func(call: Seq[Value] => Value) extends Value
 }
 def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match {
   case Expr.Str(s) => Value.Str(s)
+  case Expr.Num(i) => Value.Num(i)
   case Expr.Dict(kvs) => Value.Dict(kvs.map{case (k, v) => (k, evaluate(v, scope))})
   case Expr.Plus(left, right) =>
-    val Value.Str(leftStr) = evaluate(left, scope)
-    val Value.Str(rightStr) = evaluate(right, scope)
-    Value.Str(leftStr + rightStr)
+    (evaluate(left, scope), evaluate(right, scope)) match{
+      case (Value.Str(leftStr), Value.Str(rightStr)) => Value.Str(leftStr + rightStr)
+      case (Value.Num(leftNum), Value.Num(rightNum)) => Value.Num(leftNum + rightNum)
+    }
   case Expr.Local(name, assigned, body) =>
     val assignedValue = evaluate(assigned, scope)
     evaluate(body, scope + (name -> assignedValue))
@@ -59,6 +64,7 @@ def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match {
 }
 def serialize(v: Value): String = v match {
   case Value.Str(s) => "\"" + s + "\""
+  case Value.Num(i) => i.toString
   case Value.Dict(kvs) =>
     kvs.map{case (k, v) => "\"" + k + "\": " + serialize(v)}.mkString("{", ", ", "}")
 }
diff --git a/20.4 - Jsonnet/TestJsonnet.sc b/20.5 - JsonnetNumbers/TestJsonnet.sc
index 8cce963..4b5e005 100644
--- a/20.4 - Jsonnet/TestJsonnet.sc	
+++ b/20.5 - JsonnetNumbers/TestJsonnet.sc	
@@ -3,15 +3,17 @@ import $file.Jsonnet, Jsonnet.jsonnet
 assert(
   pprint.log(jsonnet(
     """local greeting = "Hello ";
-       local person = function (name) {
+       local bonus = 15000;
+       local person = function (name, baseSalary) {
          "name": name,
-         "welcome": greeting + name + "!"
+         "welcome": greeting + name + "!",
+         "totalSalary": baseSalary + bonus
        };
        {
-         "person1": person("Alice"),
-         "person2": person("Bob"),
-         "person3": person("Charlie")
+         "person1": person("Alice", 50000),
+         "person2": person("Bob", 60000),
+         "person3": person("Charlie", 70000)
        }"""
   )) ==
-  """{"person1": {"name": "Alice", "welcome": "Hello Alice!"}, "person2": {"name": "Bob", "welcome": "Hello Bob!"}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!"}}"""
+  """{"person1": {"name": "Alice", "welcome": "Hello Alice!", "totalSalary": 65000}, "person2": {"name": "Bob", "welcome": "Hello Bob!", "totalSalary": 75000}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!", "totalSalary": 85000}}"""
 )
```
