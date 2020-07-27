   def hello() = {
-    "Hello World!"
+    doctype("html")(
+      html(
+        head(),
+        body(
+          h1("Hello!"),
+          p("World")
+        )
+      )
+    )
   }
