# Example 19.3 - ArithmeticChained
English-like arithmetic parser, allowing more than one binary operator to be
chained together

```bash
./mill -i TestArithmetic.scala
```

## Upstream Example: [19.2 - Arithmetic](https://github.com/handsonscala/handsonscala/tree/v2/examples/19.2%20-%20Arithmetic):
Diff:
```diff
diff --git a/19.2 - Arithmetic/Arithmetic.scala b/19.3 - ArithmeticChained/Arithmetic.scala
index 75d6ec5..99ea74f 100644
--- a/19.2 - Arithmetic/Arithmetic.scala	
+++ b/19.3 - ArithmeticChained/Arithmetic.scala	
@@ -1,5 +1,6 @@
 //| mvnDeps:
 //| - com.lihaoyi::fastparse:3.1.1
+
 import fastparse.*, NoWhitespace.*
 
 enum Expr:
@@ -19,5 +20,6 @@ def number[T: P] = P(
 def ws[T: P] = P( " ".rep(1) )
 def operator[T: P] = P( "plus" | "minus" | "times" | "divide" ).!
 def expr[T: P] = P( "(" ~ parser ~ ")" | number )
-def parser[T: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr ).map:
-  case (lhs, op, rhs) => Expr.BinOp(lhs, op, rhs)
+def parser[T: P]: P[Expr] = P( expr ~ (ws ~ operator ~ ws ~ expr).rep ).map:
+  case (lhs, rights) => rights.foldLeft(lhs):
+    case (left, (op, right)) => Expr.BinOp(left, op, right)
diff --git a/19.2 - Arithmetic/TestArithmetic.scala b/19.3 - ArithmeticChained/TestArithmetic.scala
index 15a4967..573b9de 100644
--- a/19.2 - Arithmetic/TestArithmetic.scala	
+++ b/19.3 - ArithmeticChained/TestArithmetic.scala	
@@ -1,7 +1,9 @@
 //| moduleDeps: [Arithmetic.scala, Traversals.scala]
 
 def main() =
-  val t = fastparse.parse("(one plus two) times (three plus four)", parser(using _)).get.value
+  val t = fastparse.parse("one plus two times three plus four", parser(using _)).get.value
 
-  assert(stringify(t) == "((one plus two) times (three plus four))")
-  assert(evaluate(t) == 21)
+  pprint.log(stringify(t))
+  assert(stringify(t) == "(((one plus two) times three) plus four)")
+  pprint.log(evaluate(t))
+  assert(evaluate(t) == 13)
```
## Downstream Examples

- [19.4 - ArithmeticPrecedence](https://github.com/handsonscala/handsonscala/tree/v2/examples/19.4%20-%20ArithmeticPrecedence)