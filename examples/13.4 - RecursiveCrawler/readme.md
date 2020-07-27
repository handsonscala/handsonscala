# Example 13.4 - RecursiveCrawler
Parallel wikipedia crawler written in a recursive fashion

```bash
amm --class-based TestCrawler.sc
```

## Upstream Example: [13.3 - ParallelCrawler](https://github.com/handsonscala/handsonscala/tree/v1/examples/13.3%20-%20ParallelCrawler):
Diff:
```diff
diff --git a/13.3 - ParallelCrawler/Crawler.sc b/13.4 - RecursiveCrawler/Crawler.sc
index a093941..a481278 100644
--- a/13.3 - ParallelCrawler/Crawler.sc	
+++ b/13.4 - RecursiveCrawler/Crawler.sc	
@@ -1,14 +1,14 @@
 import $file.FetchLinks, FetchLinks._
 import scala.concurrent._, duration.Duration.Inf, java.util.concurrent.Executors
 implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
-def fetchAllLinksParallel(startTitle: String, depth: Int): Set[String] = {
-  var seen = Set(startTitle)
-  var current = Set(startTitle)
-  for (i <- Range(0, depth)) {
+def fetchAllLinksRec(startTitle: String, depth: Int): Set[String] = {
+  def rec(current: Set[String], seen: Set[String], recDepth: Int): Set[String] = {
+    if (recDepth >= depth) seen
+    else {
       val futures = for (title <- current) yield Future{ fetchLinks(title) }
-    val nextTitleLists = futures.map(Await.result(_, Inf))
-    current = nextTitleLists.flatten.filter(!seen.contains(_))
-    seen = seen ++ current
+      val nextTitles = futures.map(Await.result(_, Inf)).flatten
+      rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
     }
-  seen
+  }
+  rec(Set(startTitle), Set(startTitle), 0)
 }
diff --git a/13.3 - ParallelCrawler/TestCrawler.sc b/13.4 - RecursiveCrawler/TestCrawler.sc
index 687b7f7..7522fa7 100644
--- a/13.3 - ParallelCrawler/TestCrawler.sc	
+++ b/13.4 - RecursiveCrawler/TestCrawler.sc	
@@ -1,9 +1,9 @@
 import $file.Crawler, Crawler._
 
-val depth0Results = pprint.log(fetchAllLinksParallel("Singapore", 0))
-val depth1Results = pprint.log(fetchAllLinksParallel("Singapore", 1))
-val depth2Results = pprint.log(fetchAllLinksParallel("Singapore", 2))
-val depth3Results = pprint.log(fetchAllLinksParallel("Singapore", 3))
+val depth0Results = pprint.log(fetchAllLinksRec("Singapore", 0))
+val depth1Results = pprint.log(fetchAllLinksRec("Singapore", 1))
+val depth2Results = pprint.log(fetchAllLinksRec("Singapore", 2))
+val depth3Results = pprint.log(fetchAllLinksRec("Singapore", 3))
 
 pprint.log(depth0Results.size)
 pprint.log(depth1Results.size)
```
## Downstream Examples

- [13.5 - AsyncCrawler](https://github.com/handsonscala/handsonscala/tree/v1/examples/13.5%20-%20AsyncCrawler)