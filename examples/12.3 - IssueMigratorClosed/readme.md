# Example 12.3 - IssueMigratorClosed
Github issue migrator that also preserves issue open/closed status during the
migration

```bash
amm IssueMigrator.sc lihaoyi/requests-scala lihaoyi/test
```

## Upstream Example: [12.1 - IssueMigrator](https://github.com/handsonscala/handsonscala/tree/v1/examples/12.1%20-%20IssueMigrator):
Diff:
```diff
diff --git a/12.1 - IssueMigrator/IssueMigrator.sc b/12.3 - IssueMigratorClosed/IssueMigrator.sc
index 1acc2a0..215a478 100644
--- a/12.1 - IssueMigrator/IssueMigrator.sc	
+++ b/12.3 - IssueMigratorClosed/IssueMigrator.sc	
@@ -29,7 +29,8 @@
     issue("number").num.toInt,
     issue("title").str,
     issue("body").str,
-    issue("user")("login").str
+    issue("user")("login").str,
+    issue("state").str
   )
 
   val comments =
@@ -43,7 +44,7 @@
     comment("body").str
   )
 
-  val issueNums = for ((number, title, body, user) <- issueData.sortBy(_._1)) yield {
+  val issueNums = for ((number, title, body, user, state) <- issueData.sortBy(_._1)) yield {
     println(s"Creating issue $number")
     val resp = requests.post(
       s"https://api.github.com/repos/$destRepo/issues",
@@ -55,6 +56,13 @@
     )
     println(resp.statusCode)
     val newIssueNumber = ujson.read(resp.text())("number").num.toInt
+    if (state == "closed"){
+      requests.patch(
+        s"https://api.github.com/repos/$destRepo/issues/$newIssueNumber",
+        data = ujson.Obj("state" -> "closed"),
+        headers = Map("Authorization" -> s"token $token")
+      )
+    }
     (number, newIssueNumber)
   }
 
```
