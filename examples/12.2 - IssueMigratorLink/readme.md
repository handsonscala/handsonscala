# Example 12.2 - IssueMigratorLink
Github issue migrator that adds a link back from every new issue to the original
issue

```bash
./mill -i IssueMigrator.scala --src-repo com-lihaoyi/pprint --dest-repo lihaoyi/test
```

## Upstream Example: [12.1 - IssueMigrator](https://github.com/handsonscala/handsonscala/tree/v2/examples/12.1%20-%20IssueMigrator):
Diff:
```diff
diff --git a/12.1 - IssueMigrator/IssueMigrator.scala b/12.2 - IssueMigratorLink/IssueMigrator.scala
index 791040e..9dbf4c3 100644
--- a/12.1 - IssueMigrator/IssueMigrator.scala	
+++ b/12.2 - IssueMigratorLink/IssueMigrator.scala	
@@ -54,13 +54,16 @@ def main(srcRepo: String, destRepo: String) =
 
   val issueNums = for (number, title, body, user) <- issueData.sortBy(_(0)) yield
     println(s"Creating issue $number")
-    checkLimit()
 
+    val originalIssueLink =
+      s"https://github.com/$srcRepo/issues/$number"
+
+    checkLimit()
     val resp = requests.post(
       s"https://api.github.com/repos/$destRepo/issues",
       data = ujson.Obj(
         "title" -> title,
-        "body" -> s"$body\nID: $number\nOriginal Author: $user"
+        "body" -> s"$body\nID: $number\nOriginal Author: $user\nOriginal Issue: $originalIssueLink"
       ),
       headers = Map("Authorization" -> s"token $token")
     )
@@ -77,7 +80,6 @@ def main(srcRepo: String, destRepo: String) =
   do
     println(s"Commenting on issue old_id=$issueId new_id=$newIssueId")
     checkLimit()
-
     val resp = requests.post(
       s"https://api.github.com/repos/$destRepo/issues/$newIssueId/comments",
       data = ujson.Obj("body" -> s"$body\nOriginal Author:$user"),
```
