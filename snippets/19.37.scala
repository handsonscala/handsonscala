 def number[T: P] = P(
   "zero" | "one" | "two" | "three" | "four" |
   "five" | "six" | "seven" | "eight" | "nine"
 ).!.map:
   case "zero"  => Expr.Number(0); case "one"   => Expr.Number(1)
   ...
+.log

 def ws[T: P] = P( " ".rep(1) )

-def operator[T: P] = P( "plus" | "minus" | "times" | "divide" ).!
+def operator[T: P] = P( "plus" | "minus" | "times" | "divide" ).!.log