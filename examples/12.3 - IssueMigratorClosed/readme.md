# Example 12.3 - IssueMigratorClosed
Github issue migrator that also preserves issue open/closed status during the
migration

```bash
./mill -i IssueMigrator.scala --src-repo com-lihaoyi/pprint --dest-repo lihaoyi/test
```

## Upstream Example: [12.1 - IssueMigrator](https://github.com/handsonscala/handsonscala/tree/v2/examples/12.1%20-%20IssueMigrator):
Diff:
```diff
diff --git a/12.1 - IssueMigrator/IssueMigrator.scala b/12.3 - IssueMigratorClosed/IssueMigrator.scala
index 791040e..1f22f6d 100644
--- a/12.1 - IssueMigrator/IssueMigrator.scala	
+++ b/12.3 - IssueMigratorClosed/IssueMigrator.scala	
@@ -39,7 +39,8 @@ def main(srcRepo: String, destRepo: String) =
     issue("number").num.toInt,
     issue("title").str,
     issue("body").strOpt.getOrElse(""),
-    issue("user")("login").str
+    issue("user")("login").str,
+    issue("state").str
   )
 
   val comments = fetchPaginated(s"https://api.github.com/repos/$srcRepo/issues/comments")
@@ -52,10 +53,9 @@ def main(srcRepo: String, destRepo: String) =
     comment("body").str
   )
 
-  val issueNums = for (number, title, body, user) <- issueData.sortBy(_(0)) yield
+  val issueNums = for (number, title, body, user, state) <- issueData.sortBy(_(0)) yield
     println(s"Creating issue $number")
     checkLimit()
-
     val resp = requests.post(
       s"https://api.github.com/repos/$destRepo/issues",
       data = ujson.Obj(
@@ -64,9 +64,17 @@ def main(srcRepo: String, destRepo: String) =
       ),
       headers = Map("Authorization" -> s"token $token")
     )
-
     println(resp.statusCode)
     val newIssueNumber = ujson.read(resp)("number").num.toInt
+
+    if state == "closed" then
+      checkLimit()
+      requests.patch(
+        s"https://api.github.com/repos/$destRepo/issues/$newIssueNumber",
+        data = ujson.Obj("state" -> "closed"),
+        headers = Map("Authorization" -> s"token $token")
+      )
+
     (number, newIssueNumber)
 
   val issueNumMap = issueNums.toMap
@@ -83,4 +91,5 @@ def main(srcRepo: String, destRepo: String) =
       data = ujson.Obj("body" -> s"$body\nOriginal Author:$user"),
       headers = Map("Authorization" -> s"token $token")
     )
+
     println(resp.statusCode)
```
