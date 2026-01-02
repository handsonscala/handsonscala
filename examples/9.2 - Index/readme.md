# Example 9.2 - Index
Rendering an `index.html` for our static blog using Scalatags

```bash
./mill -i TestBlog.scala
```

## Upstream Example: [9.1 - Printing](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.1%20-%20Printing):
Diff:
```diff
diff --git a/9.1 - Printing/Blog.scala b/9.2 - Index/Blog.scala
index dd67c7a..113e1f7 100644
--- a/9.1 - Printing/Blog.scala	
+++ b/9.2 - Index/Blog.scala	
@@ -1,3 +1,7 @@
+//| mvnDeps:
+//| - com.lihaoyi::scalatags:0.13.1
+import scalatags.Text.all.*
+
 def main() =
   val postInfo = os
     .list(os.pwd / "post")
@@ -6,7 +10,18 @@ def main() =
       (prefix, suffix, p)
     .sortBy(_(0).toInt)
 
-  assert(
-    pprint.log(postInfo.map(t => (t._1, t._2))) ==
-    Seq("1" -> "My First Post", "2" -> "My Second Post", "3" -> "My Third Post")
+  os.remove.all(os.pwd / "out")
+  os.makeDir.all(os.pwd / "out/post")
+
+  os.write(
+    os.pwd / "out/index.html",
+    doctype("html")(
+      html(
+        body(
+          h1("Blog"),
+          for (_, suffix, _) <- postInfo
+          yield h2(suffix)
+        )
+      )
+    )
   )
diff --git a/9.2 - Index/TestBlog.scala b/9.2 - Index/TestBlog.scala
new file mode 100644
index 0000000..d1d8c70
--- /dev/null
+++ b/9.2 - Index/TestBlog.scala	
@@ -0,0 +1,9 @@
+//| moduleDeps: [Blog.scala]
+
+@main def testMain() =
+  main()
+
+  val indexHtml = pprint.log(os.read(os.pwd / "out/index.html"))
+  assert(indexHtml.contains("<h2>My First Post</h2>"))
+  assert(indexHtml.contains("<h2>My Second Post</h2>"))
+  assert(indexHtml.contains("<h2>My Third Post</h2>"))
```
## Downstream Examples

- [9.3 - Markdown](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.3%20-%20Markdown)