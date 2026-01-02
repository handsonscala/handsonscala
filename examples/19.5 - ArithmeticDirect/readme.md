# Example 19.5 - ArithmeticDirect
English-like arithmetic parser that evaluates the arithmetic without
constructing a syntax tree

```bash
./mill -i TestArithmetic.scala
```


## Upstream Example: [19.2 - Arithmetic](https://github.com/handsonscala/handsonscala/tree/v2/examples/19.2%20-%20Arithmetic):
Diff:
```diff
diff --git a/19.2 - Arithmetic/Arithmetic.scala b/19.5 - ArithmeticDirect/Arithmetic.scala
index 75d6ec5..07876e1 100644
--- a/19.2 - Arithmetic/Arithmetic.scala	
+++ b/19.5 - ArithmeticDirect/Arithmetic.scala	
@@ -2,22 +2,19 @@
 //| - com.lihaoyi::fastparse:3.1.1
 import fastparse.*, NoWhitespace.*
 
-enum Expr:
-  case BinOp(left: Expr, op: String, right: Expr)
-  case Number(value: Int)
-
 def number[T: P] = P(
   "zero" | "one" | "two" | "three" | "four" |
     "five" | "six" | "seven" | "eight" | "nine"
 ).!.map:
-  case "zero"  => Expr.Number(0); case "one"   => Expr.Number(1)
-  case "two"   => Expr.Number(2); case "three" => Expr.Number(3)
-  case "four"  => Expr.Number(4); case "five"  => Expr.Number(5)
-  case "six"   => Expr.Number(6); case "seven" => Expr.Number(7)
-  case "eight" => Expr.Number(8); case "nine"  => Expr.Number(9)
+  case "zero"  => 0; case "one"   => 1; case "two" => 2; case "three" => 3
+  case "four"  => 4; case "five"  => 5; case "six" => 6; case "seven" => 7
+  case "eight" => 8; case "nine"  => 9
 
 def ws[T: P] = P( " ".rep(1) )
 def operator[T: P] = P( "plus" | "minus" | "times" | "divide" ).!
 def expr[T: P] = P( "(" ~ parser ~ ")" | number )
-def parser[T: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr ).map:
-  case (lhs, op, rhs) => Expr.BinOp(lhs, op, rhs)
+def parser[T: P]: P[Int] = P( expr ~ ws ~ operator ~ ws ~ expr ).map:
+  case (lhs, "plus", rhs) => lhs + rhs
+  case (lhs, "minus", rhs) => lhs - rhs
+  case (lhs, "times", rhs) => lhs * rhs
+  case (lhs, "divide", rhs) => lhs / rhs
diff --git a/19.2 - Arithmetic/TestArithmetic.scala b/19.5 - ArithmeticDirect/TestArithmetic.scala
index 15a4967..bb9ba62 100644
--- a/19.2 - Arithmetic/TestArithmetic.scala	
+++ b/19.5 - ArithmeticDirect/TestArithmetic.scala	
@@ -1,7 +1,16 @@
-//| moduleDeps: [Arithmetic.scala, Traversals.scala]
+//| moduleDeps: [Arithmetic.scala]
+import fastparse.*
 
 def main() =
-  val t = fastparse.parse("(one plus two) times (three plus four)", parser(using _)).get.value
-
-  assert(stringify(t) == "((one plus two) times (three plus four))")
-  assert(evaluate(t) == 21)
+  assert(
+    pprint.log(fastparse.parse("three times seven", parser(using _))) ==
+      Parsed.Success(value = 21, index = 17)
+  )
+  assert(
+    pprint.log(fastparse.parse("(eight divide two) times (nine minus four)", parser(using _))) ==
+      Parsed.Success(value = 20, index = 42)
+  )
+  assert(
+    pprint.log(fastparse.parse("five times ((nine times eight) minus four)", parser(using _))) ==
+      Parsed.Success(value = 340, index = 42)
+  )
diff --git a/19.2 - Arithmetic/Traversals.scala b/19.2 - Arithmetic/Traversals.scala
deleted file mode 100644
index 811b135..0000000
--- a/19.2 - Arithmetic/Traversals.scala	
+++ /dev/null
@@ -1,15 +0,0 @@
-//| moduleDeps: [Arithmetic.scala]
-def stringify(e: Expr): String = e match
-  case Expr.BinOp(left, op, right) => s"(${stringify(left)} $op ${stringify(right)})"
-  case Expr.Number(0) => "zero";  case Expr.Number(1) => "one"
-  case Expr.Number(2) => "two";   case Expr.Number(3) => "three"
-  case Expr.Number(4) => "four";  case Expr.Number(5) => "five"
-  case Expr.Number(6) => "six";   case Expr.Number(7) => "seven"
-  case Expr.Number(8) => "eight"; case Expr.Number(9) => "nine"
-
-def evaluate(e: Expr): Int = e match
-  case Expr.BinOp(left, "plus", right) => evaluate(left)   + evaluate(right)
-  case Expr.BinOp(left, "minus", right) => evaluate(left)  - evaluate(right)
-  case Expr.BinOp(left, "times", right) => evaluate(left)  * evaluate(right)
-  case Expr.BinOp(left, "divide", right) => evaluate(left) / evaluate(right)
-  case Expr.Number(n) => n
```
