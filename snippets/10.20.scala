-def main(targetGitRepo: String = "") =
   ...
-
-  if targetGitRepo != "" then
-    os.call(cmd = ("git", "init"), cwd = os.pwd / "out")
-    os.call(cmd = ("git", "add", "-A"), cwd = os.pwd / "out")
-    os.call(cmd = ("git", "commit", "-am", "."), cwd = os.pwd / "out")
-    os.call(cmd = ("git", "push", targetGitRepo, "head", "-f"), cwd = os.pwd / "out")