# Example 20.7 - JsonnetStackTraces
Jsonnet interpreter which provides line and column numbers when things fail

```bash
./mill -i TestJsonnet.scala
```

## Upstream Example: [20.4 - Jsonnet](https://github.com/handsonscala/handsonscala/tree/v2/examples/20.4%20-%20Jsonnet):
Diff:
```diff
diff --git a/20.4 - Jsonnet/Jsonnet.scala b/20.7 - JsonnetStackTraces/Jsonnet.scala
index 0488512..b571839 100644
--- a/20.4 - Jsonnet/Jsonnet.scala	
+++ b/20.7 - JsonnetStackTraces/Jsonnet.scala	
@@ -1,63 +1,86 @@
 //| mvnDeps:
 //| - com.lihaoyi::fastparse:3.1.1
+
 enum Expr:
-  case Str(s: String)
-  case Ident(name: String)
-  case Plus(left: Expr, right: Expr)
-  case Dict(pairs: Map[String, Expr])
-  case Local(name: String, assigned: Expr, body: Expr)
-  case Func(argNames: Seq[String], body: Expr)
-  case Call(expr: Expr, args: Seq[Expr])
+  def index: Int
+  case Str(index: Int, s: String)
+  case Ident(index: Int, name: String)
+  case Plus(index: Int, left: Expr, right: Expr)
+  case Dict(index: Int, pairs: Map[String, Expr])
+  case Local(index: Int, name: String, assigned: Expr, body: Expr)
+  case Func(index: Int, argNames: Seq[String], body: Expr)
+  case Call(index: Int, expr: Expr, args: Seq[Expr])
 
 object Parser:
   import fastparse.*, MultiLineWhitespace.*
   def expr[T: P]: P[Expr] = P( prefixExpr ~ plus.rep ).map:
-    case (e, items) => items.foldLeft(e)(Expr.Plus(_, _))
+    case (e, items) => items.foldLeft(e):
+      case (lhs, (idx, expr)) => Expr.Plus(idx, lhs, expr)
   
   def prefixExpr[T: P]: P[Expr] = P( callExpr ~ call.rep ).map:
-    case (e, items) => items.foldLeft(e)(Expr.Call(_, _))
+    case (e, items) => items.foldLeft(e):
+      case (lhs, (idx, expr)) => Expr.Call(idx, lhs, expr)
   
   def callExpr[T: P] = P( str | dict | local | func | ident )
 
-  def str[T: P] = P( str0 ).map(Expr.Str(_))
+  def str[T: P] = P( Index ~ str0 ).map(Expr.Str.apply)
   def str0[T: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
-  def ident[T: P] = P( ident0 ).map(Expr.Ident(_))
+  def ident[T: P] = P( Index ~ ident0 ).map(Expr.Ident.apply)
   def ident0[T: P] = P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!
 
-  def dict[T: P] = P( "{" ~/ (str0 ~ ":" ~/ expr).rep(0, ",") ~ "}" )
-    .map(kvs => Expr.Dict(kvs.toMap))
+  def dict[T: P] = P( Index ~ "{" ~/ (str0 ~ ":" ~/ expr).rep(0, ",") ~ "}" )
+    .map{case (idx, kvs) => Expr.Dict(idx, kvs.toMap)}
+  
+  def local[T: P] = P( Index ~ "local" ~/ ident0 ~ "=" ~ expr ~ ";" ~ expr )
+    .map(Expr.Local(_, _, _, _))
   
-  def local[T: P] = P( "local" ~/ ident0 ~ "=" ~ expr ~ ";" ~ expr ).map(Expr.Local(_, _, _))
-  def func[T: P] = P( "function" ~/ "(" ~ ident0.rep(0, ",") ~ ")" ~ expr ).map(Expr.Func(_, _))
+  def func[T: P] = P( Index ~ "function" ~/ "(" ~ ident0.rep(0, ",") ~ ")" ~ expr ).map(Expr.Func(_, _, _))
 
-  def plus[T: P] = P( "+" ~ prefixExpr )
-  def call[T: P] = P( "(" ~/ expr.rep(0, ",") ~ ")" )
+  def plus[T: P] = P( Index ~ "+" ~ prefixExpr )
+  def call[T: P] = P( Index ~ "(" ~/ expr.rep(0, ",") ~ ")" )
 
 enum Value:
   case Str(s: String)
   case Dict(pairs: Map[String, Value])
   case Func(call: Seq[Value] => Value)
 
-def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match
-  case Expr.Str(s) => Value.Str(s)
-  case Expr.Dict(kvs) => Value.Dict(kvs.map((k, v) => (k, evaluate(v, scope))))
-  case Expr.Plus(left, right) =>
-    val Value.Str(leftStr) = evaluate(left, scope).runtimeChecked
-    val Value.Str(rightStr) = evaluate(right, scope).runtimeChecked
+class JsonnetException(val msg: String, val parent: Throwable) extends Exception(msg, parent)
+
+def evaluate(expr: Expr, scope: Map[String, Value], input: String): Value = try expr match
+  case Expr.Str(idx, s) => Value.Str(s)
+  case Expr.Dict(idx, kvs) => Value.Dict(kvs.map((k, v) => (k, evaluate(v, scope, input))))
+  case Expr.Plus(idx, left, right) =>
+    val Value.Str(leftStr) = evaluate(left, scope, input).runtimeChecked
+    val Value.Str(rightStr) = evaluate(right, scope, input).runtimeChecked
     Value.Str(leftStr + rightStr)
   
-  case Expr.Local(name, assigned, body) =>
-    val assignedValue = evaluate(assigned, scope)
-    evaluate(body, scope + (name -> assignedValue))
+  case Expr.Local(idx, name, assigned, body) =>
+    val assignedValue = evaluate(assigned, scope, input)
+    evaluate(body, scope + (name -> assignedValue), input)
   
-  case Expr.Ident(name) => scope(name)
-  case Expr.Call(expr, args) =>
-    val Value.Func(call) = evaluate(expr, scope).runtimeChecked
-    val evaluatedArgs = args.map(evaluate(_, scope))
+  case Expr.Ident(idx, name) => scope(name)
+  case Expr.Call(idx, expr, args) =>
+    val Value.Func(call) = evaluate(expr, scope, input).runtimeChecked
+    val evaluatedArgs = args.map(evaluate(_, scope, input))
     call(evaluatedArgs)
   
-  case Expr.Func(argNames, body) =>
-    Value.Func(args => evaluate(body, scope ++ argNames.zip(args)))
+  case Expr.Func(idx, argNames, body) =>
+    Value.Func(args => evaluate(body, scope ++ argNames.zip(args), input))
+catch
+  case e: JsonnetException =>
+    throw new JsonnetException(e.msg + "\n" + indexToErrorMsg(input, expr.index), e.parent)
+  case e =>
+    throw new JsonnetException("Jsonnet error " + indexToErrorMsg(input, expr.index), e)
+
+def indexToErrorMsg(input: String, index: Int) =
+  val prefix = input.take(index)
+  // +1 because line and columns normally start counting at 1 instead of 0
+  val lineNum = prefix.count(_ == '\n') + 1
+  val colNum = prefix.lastIndexOf('\n') match
+    case -1 => index + 1
+    case n => index - n
+  
+  s"at line $lineNum column $colNum"
 
 def serialize(v: Value): String = v.runtimeChecked match
   case Value.Str(s) => "\"" + s + "\""
@@ -65,4 +88,4 @@ def serialize(v: Value): String = v.runtimeChecked match
     kvs.map((k, v) => "\"" + k + "\": " + serialize(v)).mkString("{", ", ", "}")
 
 def jsonnet(input: String): String =
-  serialize(evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty))
+  serialize(evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty, input))
diff --git a/20.4 - Jsonnet/TestJsonnet.scala b/20.7 - JsonnetStackTraces/TestJsonnet.scala
index 11fb832..95c17ca 100644
--- a/20.4 - Jsonnet/TestJsonnet.scala	
+++ b/20.7 - JsonnetStackTraces/TestJsonnet.scala	
@@ -1,6 +1,6 @@
 //| moduleDeps: [Jsonnet.scala]
 
-def main(): Unit =
+def main() =
   assert(
     pprint.log(jsonnet(
       """local greeting = "Hello ";
@@ -16,3 +16,27 @@ def main(): Unit =
     )) ==
     """{"person1": {"name": "Alice", "welcome": "Hello Alice!"}, "person2": {"name": "Bob", "welcome": "Hello Bob!"}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!"}}"""
   )
+
+  val error = try { jsonnet("""local f = function(x) y; f("abc")"""); ???} catch case e => e
+  assert(
+    pprint.log(error.getMessage) ==
+    """Jsonnet error at line 1 column 23
+      |at line 1 column 27
+      |at line 1 column 1""".stripMargin
+  )
+
+  val error2 = try
+    jsonnet(
+    """local f = function(x) y;
+      |f("abc")
+      |""".stripMargin
+    )
+    ???
+  catch case e => e
+
+  assert(
+    pprint.log(error2.getMessage) ==
+    """Jsonnet error at line 1 column 23
+      |at line 2 column 2
+      |at line 1 column 1""".stripMargin
+  )
```
