# Example 9.6 - Deploy
Optionally deploying our static blog to a Git repository

```bash
./mill -i Blog.scala
./mill -i TestBlog.scala
./mill -i Blog.scala --target-git-repo git@github.com:lihaoyi/test.git
```

## Upstream Example: [9.5 - Bootstrap](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.5%20-%20Bootstrap):
Diff:
```diff
diff --git a/9.5 - Bootstrap/Blog.scala b/9.6 - Deploy/Blog.scala
index fc4d7ea..4a76700 100644
--- a/9.5 - Bootstrap/Blog.scala	
+++ b/9.6 - Deploy/Blog.scala	
@@ -3,15 +3,7 @@
 //| - org.commonmark:commonmark:0.26.0
 import scalatags.Text.all.*
 
-def mdNameToHtml(name: String) =
-  name.replace(" ", "-").toLowerCase + ".html"
-
-val bootstrapCss = link(
-  rel := "stylesheet",
-  href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
-)
-
-def main() =
+def main(targetGitRepo: String = "") =
   val postInfo = os
     .list(os.pwd / "post")
     .map: p =>
@@ -19,6 +11,14 @@ def main() =
       (prefix, suffix, p)
     .sortBy(_(0).toInt)
 
+  def mdNameToHtml(name: String) =
+    name.replace(" ", "-").toLowerCase + ".html"
+
+  val bootstrapCss = link(
+    rel := "stylesheet",
+    href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
+  )
+
   os.remove.all(os.pwd / "out")
   os.makeDir.all(os.pwd / "out/post")
 
@@ -53,3 +53,9 @@ def main() =
       )
     )
   )
+
+  if targetGitRepo != "" then
+    os.call(cmd = ("git", "init"), cwd = os.pwd / "out")
+    os.call(cmd = ("git", "add", "-A"), cwd = os.pwd / "out")
+    os.call(cmd = ("git", "commit", "-am", "."), cwd = os.pwd / "out")
+    os.call(cmd = ("git", "push", targetGitRepo, "head", "-f"), cwd = os.pwd / "out")
```
## Downstream Examples

- [9.7 - DeployTimestamp](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.7%20-%20DeployTimestamp)
- [10.6 - Blog](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.6%20-%20Blog)