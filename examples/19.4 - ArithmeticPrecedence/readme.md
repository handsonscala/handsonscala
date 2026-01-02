# Example 19.4 - ArithmeticPrecedence
English-like arithmetic parser, supporting operator precedence of chained binary
operators

```bash
./mill -i TestArithmetic.scala
```

## Upstream Example: [19.3 - ArithmeticChained](https://github.com/handsonscala/handsonscala/tree/v2/examples/19.3%20-%20ArithmeticChained):
Diff:
```diff
diff --git a/19.3 - ArithmeticChained/Arithmetic.scala b/19.4 - ArithmeticPrecedence/Arithmetic.scala
index 99ea74f..8fe7d27 100644
--- a/19.3 - ArithmeticChained/Arithmetic.scala	
+++ b/19.4 - ArithmeticPrecedence/Arithmetic.scala	
@@ -1,6 +1,5 @@
 //| mvnDeps:
 //| - com.lihaoyi::fastparse:3.1.1
-
 import fastparse.*, NoWhitespace.*
 
 enum Expr:
@@ -20,6 +19,25 @@ def number[T: P] = P(
 def ws[T: P] = P( " ".rep(1) )
 def operator[T: P] = P( "plus" | "minus" | "times" | "divide" ).!
 def expr[T: P] = P( "(" ~ parser ~ ")" | number )
+
+val precedence = Map("plus" -> 1, "minus" -> 1, "times" -> 2, "divide" -> 2)
+
+def climb(left: Expr,
+          rights: collection.mutable.ArrayDeque[(String, Expr)],
+          minPrec: Int): Expr =
+  var result = left
+  var done = false
+  while !done do
+    rights.headOption match
+      case None => done = true
+      case Some((op, next)) =>
+        val prec: Int = precedence(op)
+        if prec < minPrec then done = true
+        else
+          rights.removeHead()
+          val rhs = climb(next, rights, prec + 1)
+          result = Expr.BinOp(result, op, rhs)
+  result
+
 def parser[T: P]: P[Expr] = P( expr ~ (ws ~ operator ~ ws ~ expr).rep ).map:
-  case (lhs, rights) => rights.foldLeft(lhs):
-    case (left, (op, right)) => Expr.BinOp(left, op, right)
+  case (lhs, rights) => climb(lhs, rights.to(collection.mutable.ArrayDeque), 1)
diff --git a/19.3 - ArithmeticChained/TestArithmetic.scala b/19.4 - ArithmeticPrecedence/TestArithmetic.scala
index 573b9de..198e084 100644
--- a/19.3 - ArithmeticChained/TestArithmetic.scala	
+++ b/19.4 - ArithmeticPrecedence/TestArithmetic.scala	
@@ -4,6 +4,6 @@ def main() =
   val t = fastparse.parse("one plus two times three plus four", parser(using _)).get.value
 
   pprint.log(stringify(t))
-  assert(stringify(t) == "(((one plus two) times three) plus four)")
+  assert(stringify(t) == "((one plus (two times three)) plus four)")
   pprint.log(evaluate(t))
-  assert(evaluate(t) == 13)
+  assert(evaluate(t) == 11)
```
