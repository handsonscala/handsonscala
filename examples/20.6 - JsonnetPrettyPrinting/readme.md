# Example 20.6 - JsonnetPrettyPrinting
Jsonnet interpreter using the uJson library for pretty-printing the output JSON

```bash
./mill -i TestJsonnet.scala
```

## Upstream Example: [20.4 - Jsonnet](https://github.com/handsonscala/handsonscala/tree/v2/examples/20.4%20-%20Jsonnet):
Diff:
```diff
diff --git a/20.4 - Jsonnet/Jsonnet.scala b/20.6 - JsonnetPrettyPrinting/Jsonnet.scala
index 0488512..adcd978 100644
--- a/20.4 - Jsonnet/Jsonnet.scala	
+++ b/20.6 - JsonnetPrettyPrinting/Jsonnet.scala	
@@ -1,5 +1,6 @@
 //| mvnDeps:
 //| - com.lihaoyi::fastparse:3.1.1
+
 enum Expr:
   case Str(s: String)
   case Ident(name: String)
@@ -59,10 +60,12 @@ def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match
   case Expr.Func(argNames, body) =>
     Value.Func(args => evaluate(body, scope ++ argNames.zip(args)))
 
-def serialize(v: Value): String = v.runtimeChecked match
-  case Value.Str(s) => "\"" + s + "\""
-  case Value.Dict(kvs) =>
-    kvs.map((k, v) => "\"" + k + "\": " + serialize(v)).mkString("{", ", ", "}")
+def serialize(v: Value): ujson.Value = v.runtimeChecked match
+  case Value.Str(s) => ujson.Str(s)
+  case Value.Dict(kvs) => ujson.Obj.from(kvs.map((k, v) => (k, serialize(v))))
 
 def jsonnet(input: String): String =
-  serialize(evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty))
+  ujson.write(
+    serialize(evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty)),
+    indent = 2
+  )
diff --git a/20.4 - Jsonnet/TestJsonnet.scala b/20.6 - JsonnetPrettyPrinting/TestJsonnet.scala
index 11fb832..37cc82c 100644
--- a/20.4 - Jsonnet/TestJsonnet.scala	
+++ b/20.6 - JsonnetPrettyPrinting/TestJsonnet.scala	
@@ -1,6 +1,6 @@
 //| moduleDeps: [Jsonnet.scala]
 
-def main(): Unit =
+def main() =
   assert(
     pprint.log(jsonnet(
       """local greeting = "Hello ";
@@ -14,5 +14,18 @@ def main(): Unit =
            "person3": person("Charlie")
          }"""
     )) ==
-    """{"person1": {"name": "Alice", "welcome": "Hello Alice!"}, "person2": {"name": "Bob", "welcome": "Hello Bob!"}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!"}}"""
+    """{
+  "person1": {
+    "name": "Alice",
+    "welcome": "Hello Alice!"
+  },
+  "person2": {
+    "name": "Bob",
+    "welcome": "Hello Bob!"
+  },
+  "person3": {
+    "name": "Charlie",
+    "welcome": "Hello Charlie!"
+  }
+}""".stripMargin
   )
```
