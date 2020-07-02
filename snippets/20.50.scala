 def serialize(v: Value): String = v match {
   case Value.Str(s) => "\"" + s + "\""
+  case Value.Dict(kvs) =>
+    kvs.map{case (k, v) => "\"" + k + "\": " + serialize(v)}.mkString("{", ", ", "}")
 }
