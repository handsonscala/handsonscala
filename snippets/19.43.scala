-def expr[T: P] = P( "(" ~ parser ~ ")" | number )
+def expr[T: P] = P( "(" ~/ parser ~ ")" | number )