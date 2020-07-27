-def expr[_: P] = P( "(" ~ parser ~ ")" | number )
+def expr[_: P] = P( "(" ~/ parser ~ ")" | number )