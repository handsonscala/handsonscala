# Example 13.8 - AsyncCrawlerThrottled
Asynchronous wikipedia crawler which limits the number of open requests

```bash
./mill -i TestCrawler.scala
```

## Upstream Example: [13.5 - AsyncCrawler](https://github.com/handsonscala/handsonscala/tree/v2/examples/13.5%20-%20AsyncCrawler):
Diff:
```diff
diff --git a/13.5 - AsyncCrawler/Crawler.scala b/13.8 - AsyncCrawlerThrottled/Crawler.scala
index fa0195a..b97aa94 100644
--- a/13.5 - AsyncCrawler/Crawler.scala	
+++ b/13.8 - AsyncCrawlerThrottled/Crawler.scala	
@@ -1,18 +1,23 @@
 //| moduleDeps: [FetchLinksAsync.scala]
 import scala.concurrent.*, java.util.concurrent.Executors
-
 val service = Executors.newFixedThreadPool(8)
 given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)
-
-def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] =
-  def rec(current: Set[String], seen: Set[String], recDepth: Int): Future[Set[String]] =
-    if recDepth >= depth then Future.successful(seen)
+def fetchAllLinksAsync(startTitle: String, maxDepth: Int, maxConcurrency: Int): Future[Set[String]] =
+  def rec(current: Seq[(String, Int)], seen: Set[String]): Future[Set[String]] =
+    pprint.log((maxDepth, current.size, seen.size))
+    if current.isEmpty then Future.successful(seen)
     else
-      val futures = for title <- current yield fetchLinksAsync(title)
-      Future.sequence(futures)
-        .map: nextTitleLists =>
-          val nextTitles = nextTitleLists.flatten
-          rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
-        .flatten
+      val (throttled, remaining) = current.splitAt(maxConcurrency)
+      val futures =
+        for (title, depth) <- throttled
+        yield fetchLinksAsync(title).map((_, depth))
 
-  rec(Set(startTitle), Set(startTitle), 0)
+      Future.sequence(futures).map: nextTitleLists =>
+        val flattened = for
+          (titles, depth) <- nextTitleLists
+          title <- titles
+          if !seen.contains(title) && depth < maxDepth
+        yield (title, depth + 1)
+        rec(remaining ++ flattened, seen ++ flattened.map(_(0)))
+      .flatten
+  rec(Seq(startTitle -> 0), Set(startTitle))
diff --git a/13.5 - AsyncCrawler/FetchLinksAsync.scala b/13.8 - AsyncCrawlerThrottled/FetchLinksAsync.scala
index ac8efaf..2c66c07 100644
--- a/13.5 - AsyncCrawler/FetchLinksAsync.scala	
+++ b/13.8 - AsyncCrawlerThrottled/FetchLinksAsync.scala	
@@ -12,7 +12,8 @@ def fetchLinksAsync(title: String)(using ec: ExecutionContext): Future[Seq[Strin
     .execute()
 
   listenableFut.addListener(() => p.success(listenableFut.get().getResponseBody), null)
-  p.future.map: responseBody =>
+  val scalaFut: Future[String] = p.future
+  scalaFut.map: responseBody =>
     for
       page <- ujson.read(responseBody)("query")("pages").obj.values.toSeq
       links <- page.obj.get("links").toSeq
diff --git a/13.5 - AsyncCrawler/TestCrawler.scala b/13.8 - AsyncCrawlerThrottled/TestCrawler.scala
index 40a5736..a15d463 100644
--- a/13.5 - AsyncCrawler/TestCrawler.scala	
+++ b/13.8 - AsyncCrawlerThrottled/TestCrawler.scala	
@@ -1,11 +1,16 @@
-//| moduleDeps: [Crawler.scala, FetchLinksAsync.scala]
+//| moduleDeps: [Crawler.scala]
+import scala.concurrent.*, duration.Duration.Inf
+
 def main() =
-  import scala.concurrent._, duration.Duration.Inf
+  val depth0Results = Await.result(fetchAllLinksAsync("Singapore", 0, 16), Inf)
+  val depth1Results = Await.result(fetchAllLinksAsync("Singapore", 1, 16), Inf)
+  val depth2Results = Await.result(fetchAllLinksAsync("Singapore", 2, 16), Inf)
+  val depth3Results = Await.result(fetchAllLinksAsync("Singapore", 3, 16), Inf)
 
-  val depth0Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 0), Inf): Set[String])
-  val depth1Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 1), Inf): Set[String])
-  val depth2Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 2), Inf): Set[String])
-  val depth3Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 3), Inf): Set[String])
+  pprint.log(depth0Results)
+  pprint.log(depth1Results)
+  pprint.log(depth2Results)
+  pprint.log(depth3Results)
 
   pprint.log(depth0Results.size)
   pprint.log(depth1Results.size)
@@ -30,8 +35,10 @@ def main() =
         ".sg"
       )
     )
+
     assert(depth1Results.subsetOf(depth2Results))
     assert(depth1Results.size < depth2Results.size)
+
     assert(depth2Results.subsetOf(depth3Results))
     assert(depth2Results.size < depth3Results.size)
   finally
```
