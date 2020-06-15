# Example 20.7 - JsonnetStackTraces
Jsonnet interpreter which provides line and column numbers when things fail

```bash
amm TestJsonnet.sc
```

## Upstream Example: [20.4 - Jsonnet](https://github.com/handsonscala/handsonscala/tree/v1/examples/20.4%20-%20Jsonnet):
Diff:
```diff
diff --git a/20.4 - Jsonnet/Jsonnet.sc b/20.7 - JsonnetStackTraces/Jsonnet.sc
index 4ebe482..dd1e8a5 100644
--- a/20.4 - Jsonnet/Jsonnet.sc	
+++ b/20.7 - JsonnetStackTraces/Jsonnet.sc	
@@ -1,37 +1,43 @@
-sealed trait Expr
+sealed trait Expr{
+  def index: Int
+}
 object Expr{
-  case class Str(s: String) extends Expr
-  case class Ident(name: String) extends Expr
-  case class Plus(left: Expr, right: Expr) extends Expr
-  case class Dict(pairs: Map[String, Expr]) extends Expr
-  case class Local(name: String, assigned: Expr, body: Expr) extends Expr
-  case class Func(argNames: Seq[String], body: Expr) extends Expr
-  case class Call(expr: Expr, args: Seq[Expr]) extends Expr
+  case class Str(index: Int, s: String) extends Expr
+  case class Ident(index: Int, name: String) extends Expr
+  case class Plus(index: Int, left: Expr, right: Expr) extends Expr
+  case class Dict(index: Int, pairs: Map[String, Expr]) extends Expr
+  case class Local(index: Int, name: String, assigned: Expr, body: Expr) extends Expr
+  case class Func(index: Int, argNames: Seq[String], body: Expr) extends Expr
+  case class Call(index: Int, expr: Expr, args: Seq[Expr]) extends Expr
 }
 object Parser {
   import fastparse._, MultiLineWhitespace._
   def expr[_: P]: P[Expr] = P( prefixExpr ~ plus.rep ).map{
-    case (e, items) => items.foldLeft(e)(Expr.Plus(_, _))
+    case (e, items) => items.foldLeft(e){
+      case (lhs, (idx, expr)) => Expr.Plus(idx, lhs, expr)
+    }
   }
   def prefixExpr[_: P]: P[Expr] = P( callExpr ~ call.rep ).map{
-    case (e, items) => items.foldLeft(e)(Expr.Call(_, _))
+    case (e, items) => items.foldLeft(e){
+      case (lhs, (idx, expr)) => Expr.Call(idx, lhs, expr)
+    }
   }
   def callExpr[_: P] = P( str | dict | local | func | ident )
 
-  def str[_: P] = P( str0 ).map(Expr.Str)
+  def str[_: P] = P( Index ~ str0 ).map(Expr.Str.tupled)
   def str0[_: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
-  def ident[_: P] = P( ident0 ).map(Expr.Ident)
+  def ident[_: P] = P( Index ~ ident0 ).map(Expr.Ident.tupled)
   def ident0[_: P] = P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!
 
-  def dict[_: P] = P( "{" ~/ (str0 ~ ":" ~/ expr).rep(0, ",") ~ "}" )
-    .map(kvs => Expr.Dict(kvs.toMap))
-  def local[_: P] = P( "local" ~/ ident0 ~ "=" ~ expr ~ ";" ~ expr )
+  def dict[_: P] = P( Index ~ "{" ~/ (str0 ~ ":" ~/ expr).rep(0, ",") ~ "}" )
+    .map{case (idx, kvs) => Expr.Dict(idx, kvs.toMap)}
+  def local[_: P] = P( Index ~ "local" ~/ ident0 ~ "=" ~ expr ~ ";" ~ expr )
     .map(Expr.Local.tupled)
-  def func[_: P] = P( "function" ~/ "(" ~ ident0.rep(0, ",") ~ ")" ~ expr )
+  def func[_: P] = P( Index ~ "function" ~/ "(" ~ ident0.rep(0, ",") ~ ")" ~ expr )
     .map(Expr.Func.tupled)
 
-  def plus[_: P] = P( "+" ~ prefixExpr )
-  def call[_: P] = P( "(" ~/ expr.rep(0, ",") ~ ")" )
+  def plus[_: P] = P( Index ~ "+" ~ prefixExpr )
+  def call[_: P] = P( Index ~ "(" ~/ expr.rep(0, ",") ~ ")" )
 }
 sealed trait Value
 object Value{
@@ -40,23 +46,41 @@ object Value{
   case class Func(call: Seq[Value] => Value) extends Value
 }
 
-def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match {
-  case Expr.Str(s) => Value.Str(s)
-  case Expr.Dict(kvs) => Value.Dict(kvs.map{case (k, v) => (k, evaluate(v, scope))})
-  case Expr.Plus(left, right) =>
-    val Value.Str(leftStr) = evaluate(left, scope)
-    val Value.Str(rightStr) = evaluate(right, scope)
+class JsonnetException(val msg: String, val parent: Throwable) extends Exception(msg, parent)
+def evaluate(expr: Expr, scope: Map[String, Value], input: String): Value = try expr match {
+  case Expr.Str(idx, s) => Value.Str(s)
+  case Expr.Dict(idx, kvs) => Value.Dict(kvs.map{case (k, v) => (k, evaluate(v, scope, input))})
+  case Expr.Plus(idx, left, right) =>
+    val Value.Str(leftStr) = evaluate(left, scope, input)
+    val Value.Str(rightStr) = evaluate(right, scope, input)
     Value.Str(leftStr + rightStr)
-  case Expr.Local(name, assigned, body) =>
-    val assignedValue = evaluate(assigned, scope)
-    evaluate(body, scope + (name -> assignedValue))
-  case Expr.Ident(name) => scope(name)
-  case Expr.Call(expr, args) =>
-    val Value.Func(call) = evaluate(expr, scope)
-    val evaluatedArgs = args.map(evaluate(_, scope))
+  case Expr.Local(idx, name, assigned, body) =>
+    val assignedValue = evaluate(assigned, scope, input)
+    evaluate(body, scope + (name -> assignedValue), input)
+  case Expr.Ident(idx, name) => scope(name)
+  case Expr.Call(idx, expr, args) =>
+    val Value.Func(call) = evaluate(expr, scope, input)
+    val evaluatedArgs = args.map(evaluate(_, scope, input))
     call(evaluatedArgs)
-  case Expr.Func(argNames, body) =>
-    Value.Func(args => evaluate(body, scope ++ argNames.zip(args)))
+  case Expr.Func(idx, argNames, body) =>
+    Value.Func(args => evaluate(body, scope ++ argNames.zip(args), input))
+} catch{
+  case e: JsonnetException =>
+    throw new JsonnetException(e.msg + "\n" + indexToErrorMsg(input, expr.index), e.parent)
+
+  case e =>
+    throw new JsonnetException("Jsonnet error " + indexToErrorMsg(input, expr.index), e)
+}
+
+def indexToErrorMsg(input: String, index: Int) = {
+  val prefix = input.take(index)
+  // +1 because line and columns normally start counting at 1 instead of 0
+  val lineNum = prefix.count(_ == '\n') + 1
+  val colNum = prefix.lastIndexOf('\n') match{
+    case -1 => index + 1
+    case n => index - n
+  }
+  s"at line $lineNum column $colNum"
 }
 
 def serialize(v: Value): String = v match {
@@ -66,5 +90,5 @@ def serialize(v: Value): String = v match {
 }
 
 def jsonnet(input: String): String = {
-  serialize(evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty))
+  serialize(evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty, input))
 }
diff --git a/20.4 - Jsonnet/TestJsonnet.sc b/20.7 - JsonnetStackTraces/TestJsonnet.sc
index 8cce963..c13d73a 100644
--- a/20.4 - Jsonnet/TestJsonnet.sc	
+++ b/20.7 - JsonnetStackTraces/TestJsonnet.sc	
@@ -15,3 +15,27 @@ assert(
   )) ==
   """{"person1": {"name": "Alice", "welcome": "Hello Alice!"}, "person2": {"name": "Bob", "welcome": "Hello Bob!"}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!"}}"""
 )
+
+val error = try { jsonnet("""local f = function(x) y; f("abc")"""); ???} catch{case e => e}
+assert(
+  pprint.log(error.getMessage) ==
+  """Jsonnet error at line 1 column 23
+    |at line 1 column 27
+    |at line 1 column 1""".stripMargin
+)
+
+val error2 = try {
+  jsonnet(
+  """local f = function(x) y;
+    |f("abc")
+    |""".stripMargin
+  )
+  ???
+} catch{case e => e}
+
+assert(
+  pprint.log(error2.getMessage) ==
+  """Jsonnet error at line 1 column 23
+    |at line 2 column 2
+    |at line 1 column 1""".stripMargin
+)
```
