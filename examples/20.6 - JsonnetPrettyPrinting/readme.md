# Example 20.6 - JsonnetPrettyPrinting
Jsonnet interpreter using the uJson library for pretty-printing the output JSON

```bash
amm TestJsonnet.sc
```

## Upstream Example: [20.4 - Jsonnet](https://github.com/handsonscala/handsonscala/tree/v1/examples/20.4%20-%20Jsonnet):
Diff:
```diff
diff --git a/20.4 - Jsonnet/Jsonnet.sc b/20.6 - JsonnetPrettyPrinting/Jsonnet.sc
index 24df803..503548d 100644
--- a/20.4 - Jsonnet/Jsonnet.sc	
+++ b/20.6 - JsonnetPrettyPrinting/Jsonnet.sc	
@@ -57,11 +57,13 @@ def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match {
   case Expr.Func(argNames, body) =>
     Value.Func(args => evaluate(body, scope ++ argNames.zip(args)))
 }
-def serialize(v: Value): String = v match {
-  case Value.Str(s) => "\"" + s + "\""
-  case Value.Dict(kvs) =>
-    kvs.map{case (k, v) => "\"" + k + "\": " + serialize(v)}.mkString("{", ", ", "}")
+def serialize(v: Value): ujson.Value = v match {
+  case Value.Str(s) => ujson.Str(s)
+  case Value.Dict(kvs) => ujson.Obj.from(kvs.map{case (k, v) => (k, serialize(v))})
 }
 def jsonnet(input: String): String = {
-  serialize(evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty))
+  ujson.write(
+    serialize(evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty)),
+    indent = 2
+  )
 }
diff --git a/20.4 - Jsonnet/TestJsonnet.sc b/20.6 - JsonnetPrettyPrinting/TestJsonnet.sc
index 8cce963..dac9699 100644
--- a/20.4 - Jsonnet/TestJsonnet.sc	
+++ b/20.6 - JsonnetPrettyPrinting/TestJsonnet.sc	
@@ -13,5 +13,18 @@ assert(
          "person3": person("Charlie")
        }"""
   )) ==
-  """{"person1": {"name": "Alice", "welcome": "Hello Alice!"}, "person2": {"name": "Bob", "welcome": "Hello Bob!"}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!"}}"""
+  """{
+    |  "person1": {
+    |    "name": "Alice",
+    |    "welcome": "Hello Alice!"
+    |  },
+    |  "person2": {
+    |    "name": "Bob",
+    |    "welcome": "Hello Bob!"
+    |  },
+    |  "person3": {
+    |    "name": "Charlie",
+    |    "welcome": "Hello Charlie!"
+    |  }
+    |}""".stripMargin
 )
```
