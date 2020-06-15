 def serialize(v: Value): String = v match {
+  case Value.Str(s) => "\"" + s + "\""
 }
