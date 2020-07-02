# Example 19.3 - ArithmeticChained
English-like arithmetic parser, allowing more than one binary operator to be
chained together

```bash
amm TestArithmetic.sc
```

## Upstream Example: [19.2 - Arithmetic](https://github.com/handsonscala/handsonscala/tree/v1/examples/19.2%20-%20Arithmetic):
Diff:
```diff
diff --git a/19.2 - Arithmetic/Arithmetic.sc b/19.3 - ArithmeticChained/Arithmetic.sc
index 66f0349..9771e29 100644
--- a/19.2 - Arithmetic/Arithmetic.sc	
+++ b/19.3 - ArithmeticChained/Arithmetic.sc	
@@ -16,6 +16,8 @@ def number[_: P] = P(
 def ws[_: P] = P( " ".rep(1) )
 def operator[_: P] = P( "plus" | "minus" | "times" | "divide" ).!
 def expr[_: P] = P( "(" ~ parser ~ ")" | number )
-def parser[_: P]: P[Expr] = P( expr ~ ws ~ operator ~ ws ~ expr ).map{
-  case (lhs, op, rhs) => BinOp(lhs, op, rhs)
+def parser[_: P]: P[Expr] = P( expr ~ (ws ~ operator ~ ws ~ expr).rep ).map{
+  case (lhs, rights) => rights.foldLeft(lhs){
+    case (left, (op, right)) => BinOp(left, op, right)
+  }
 }
diff --git a/19.2 - Arithmetic/TestArithmetic.sc b/19.3 - ArithmeticChained/TestArithmetic.sc
index dea4793..9f68f2f 100644
--- a/19.2 - Arithmetic/TestArithmetic.sc	
+++ b/19.3 - ArithmeticChained/TestArithmetic.sc	
@@ -1,7 +1,9 @@
 import $file.Arithmetic, Arithmetic._
 import $file.Traversals, Traversals._
 
-val t = fastparse.parse("(one plus two) times (three plus four)", parser(_)).get.value
+val t = fastparse.parse("one plus two times three plus four", parser(_)).get.value
 
-assert(stringify(t) == "((one plus two) times (three plus four))")
-assert(evaluate(t) == 21)
+pprint.log(stringify(t))
+assert(stringify(t) == "(((one plus two) times three) plus four)")
+pprint.log(evaluate(t))
+assert(evaluate(t) == 13)
```
## Downstream Examples

- [19.4 - ArithmeticPrecedence](https://github.com/handsonscala/handsonscala/tree/v1/examples/19.4%20-%20ArithmeticPrecedence)