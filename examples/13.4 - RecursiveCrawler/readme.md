# Example 13.4 - RecursiveCrawler
Parallel wikipedia crawler written in a recursive fashion

```bash
./mill -i TestCrawler.scala
```

## Upstream Example: [13.3 - ParallelCrawler](https://github.com/handsonscala/handsonscala/tree/v2/examples/13.3%20-%20ParallelCrawler):
Diff:
```diff
diff --git a/13.3 - ParallelCrawler/Crawler.scala b/13.4 - RecursiveCrawler/Crawler.scala
index 5a1a75d..dcf3ffd 100644
--- a/13.3 - ParallelCrawler/Crawler.scala	
+++ b/13.4 - RecursiveCrawler/Crawler.scala	
@@ -1,16 +1,14 @@
 //| moduleDeps: [FetchLinks.scala]
 import scala.concurrent.*, duration.Duration.Inf, java.util.concurrent.Executors
-import duration.*
 val service = Executors.newFixedThreadPool(8)
 given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)
-def fetchAllLinksParallel(startTitle: String, depth: Int): Set[String] =
-  var seen = Set(startTitle)
-  var current = Set(startTitle)
 
-  for i <- Range(0, depth) do
-    val futures = for (title <- current) yield Future{ fetchLinks(title) }
-    val nextTitleLists = futures.map(Await.result(_, Inf))
-    current = nextTitleLists.flatten.filter(!seen.contains(_))
-    seen = seen ++ current
+def fetchAllLinksRec(startTitle: String, depth: Int): Set[String] =
+  def rec(current: Set[String], seen: Set[String], recDepth: Int): Set[String] =
+    if recDepth >= depth then seen
+    else
+      val futures = for title <- current yield Future{ fetchLinks(title) }
+      val nextTitles = futures.map(Await.result(_, Inf)).flatten
+      rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
 
-  seen
+  rec(Set(startTitle), Set(startTitle), 0)
diff --git a/13.3 - ParallelCrawler/TestCrawler.scala b/13.4 - RecursiveCrawler/TestCrawler.scala
index 212dbfe..214d958 100644
--- a/13.3 - ParallelCrawler/TestCrawler.scala	
+++ b/13.4 - RecursiveCrawler/TestCrawler.scala	
@@ -1,10 +1,10 @@
-//| moduleDeps: [Crawler.scala]
+//| moduleDeps: [Crawler.scala, FetchLinks.scala]
 
 def main() =
-  val depth0Results = pprint.log(fetchAllLinksParallel("Singapore", 0))
-  val depth1Results = pprint.log(fetchAllLinksParallel("Singapore", 1))
-  val depth2Results = pprint.log(fetchAllLinksParallel("Singapore", 2))
-  val depth3Results = pprint.log(fetchAllLinksParallel("Singapore", 3))
+  val depth0Results = pprint.log(fetchAllLinksRec("Singapore", 0))
+  val depth1Results = pprint.log(fetchAllLinksRec("Singapore", 1))
+  val depth2Results = pprint.log(fetchAllLinksRec("Singapore", 2))
+  val depth3Results = pprint.log(fetchAllLinksRec("Singapore", 3))
 
   pprint.log(depth0Results.size)
   pprint.log(depth1Results.size)
```
## Downstream Examples

- [13.5 - AsyncCrawler](https://github.com/handsonscala/handsonscala/tree/v2/examples/13.5%20-%20AsyncCrawler)