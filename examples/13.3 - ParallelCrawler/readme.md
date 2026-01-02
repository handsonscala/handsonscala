# Example 13.3 - ParallelCrawler
Simple batch-by-batch parallel wikipedia crawler, using Futures and
Requests-Scala

```bash
./mill -i TestCrawler.scala
```

## Upstream Example: [13.2 - Crawler](https://github.com/handsonscala/handsonscala/tree/v2/examples/13.2%20-%20Crawler):
Diff:
```diff
diff --git a/13.2 - Crawler/Crawler.scala b/13.3 - ParallelCrawler/Crawler.scala
index 16b2580..5a1a75d 100644
--- a/13.2 - Crawler/Crawler.scala	
+++ b/13.3 - ParallelCrawler/Crawler.scala	
@@ -1,10 +1,15 @@
 //| moduleDeps: [FetchLinks.scala]
-def fetchAllLinks(startTitle: String, depth: Int): Set[String] =
+import scala.concurrent.*, duration.Duration.Inf, java.util.concurrent.Executors
+import duration.*
+val service = Executors.newFixedThreadPool(8)
+given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)
+def fetchAllLinksParallel(startTitle: String, depth: Int): Set[String] =
   var seen = Set(startTitle)
   var current = Set(startTitle)
 
   for i <- Range(0, depth) do
-    val nextTitleLists = for title <- current yield fetchLinks(title)
+    val futures = for (title <- current) yield Future{ fetchLinks(title) }
+    val nextTitleLists = futures.map(Await.result(_, Inf))
     current = nextTitleLists.flatten.filter(!seen.contains(_))
     seen = seen ++ current
 
diff --git a/13.2 - Crawler/FetchLinks.scala b/13.3 - ParallelCrawler/FetchLinks.scala
index 6db222f..4d2daf2 100644
--- a/13.2 - Crawler/FetchLinks.scala	
+++ b/13.3 - ParallelCrawler/FetchLinks.scala	
@@ -8,7 +8,6 @@ def fetchLinks(title: String): Seq[String] =
       "format" -> "json"
     )
   )
-
   for
     page <- ujson.read(resp)("query")("pages").obj.values.toSeq
     links <- page.obj.get("links").toSeq
diff --git a/13.2 - Crawler/TestCrawler.scala b/13.3 - ParallelCrawler/TestCrawler.scala
index 47036e3..212dbfe 100644
--- a/13.2 - Crawler/TestCrawler.scala	
+++ b/13.3 - ParallelCrawler/TestCrawler.scala	
@@ -1,15 +1,17 @@
 //| moduleDeps: [Crawler.scala]
+
 def main() =
-  val depth0Results = pprint.log(fetchAllLinks("Singapore", 0))
-  val depth1Results = pprint.log(fetchAllLinks("Singapore", 1))
-  val depth2Results = pprint.log(fetchAllLinks("Singapore", 2))
-  val depth3Results = pprint.log(fetchAllLinks("Singapore", 3))
+  val depth0Results = pprint.log(fetchAllLinksParallel("Singapore", 0))
+  val depth1Results = pprint.log(fetchAllLinksParallel("Singapore", 1))
+  val depth2Results = pprint.log(fetchAllLinksParallel("Singapore", 2))
+  val depth3Results = pprint.log(fetchAllLinksParallel("Singapore", 3))
 
   pprint.log(depth0Results.size)
   pprint.log(depth1Results.size)
   pprint.log(depth2Results.size)
   pprint.log(depth3Results.size)
 
+  try
     assert(depth0Results == Set("Singapore"))
     assert(
       depth1Results ==
@@ -33,3 +35,5 @@ def main() =
 
     assert(depth2Results.subsetOf(depth3Results))
     assert(depth2Results.size < depth3Results.size)
+  finally
+    service.shutdown()
```
## Downstream Examples

- [13.4 - RecursiveCrawler](https://github.com/handsonscala/handsonscala/tree/v2/examples/13.4%20-%20RecursiveCrawler)