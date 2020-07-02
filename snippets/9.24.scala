  import $ivy.`com.lihaoyi::scalatags:0.9.1`, scalatags.Text.all._
  import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`

+ @main def main(targetGitRepo: String = ""): Unit = {

    ...

+   if (targetGitRepo != "") {
+     os.proc("git", "init").call(cwd = os.pwd / "out")
+     os.proc("git", "add", "-A").call(cwd = os.pwd / "out")
+     os.proc("git", "commit", "-am", ".").call(cwd = os.pwd / "out")
+     os.proc("git", "push", targetGitRepo, "head", "-f").call(cwd = os.pwd / "out")
+   }
+ }