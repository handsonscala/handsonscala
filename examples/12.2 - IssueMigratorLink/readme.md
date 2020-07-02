# Example 12.2 - IssueMigratorLink
Github issue migrator that adds a link back from every new issue to the original
issue

```bash
amm IssueMigrator.sc lihaoyi/requests-scala lihaoyi/test
```

## Upstream Example: [12.1 - IssueMigrator](https://github.com/handsonscala/handsonscala/tree/v1/examples/12.1%20-%20IssueMigrator):
Diff:
```diff
diff --git a/12.1 - IssueMigrator/IssueMigrator.sc b/12.2 - IssueMigratorLink/IssueMigrator.sc
index 1acc2a0..3729de5 100644
--- a/12.1 - IssueMigrator/IssueMigrator.sc	
+++ b/12.2 - IssueMigratorLink/IssueMigrator.sc	
@@ -45,11 +45,15 @@
 
   val issueNums = for ((number, title, body, user) <- issueData.sortBy(_._1)) yield {
     println(s"Creating issue $number")
+
+    val originalIssueLink =
+      s"https://github.com/$srcRepo/issues/$number"
+
     val resp = requests.post(
       s"https://api.github.com/repos/$destRepo/issues",
       data = ujson.Obj(
         "title" -> title,
-        "body" -> s"$body\nID: $number\nOriginal Author: $user"
+        "body" -> s"$body\nID: $number\nOriginal Author: $user\nOriginal Issue: $originalIssueLink"
       ),
       headers = Map("Authorization" -> s"token $token")
     )
```
