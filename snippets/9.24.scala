 //| mvnDeps:
 //| - com.lihaoyi::scalatags:0.13.1
 //| - org.commonmark:commonmark:0.26.0
 import scalatags.Text.all.*
+import mainargs.*

+ParserForMethods(this).runOrExit(args) // `args` is available at the top-level

+def main(targetGitRepo: String = "") =
   ...
+
+  if targetGitRepo != "" then
+    os.call(cmd = ("git", "init"), cwd = os.pwd / "out")
+    os.call(cmd = ("git", "add", "-A"), cwd = os.pwd / "out")
+    os.call(cmd = ("git", "commit", "-am", "."), cwd = os.pwd / "out")
+    os.call(cmd = ("git", "push", targetGitRepo, "head", "-f"), cwd = os.pwd / "out")