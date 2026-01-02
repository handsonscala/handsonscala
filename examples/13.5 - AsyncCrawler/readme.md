# Example 13.5 - AsyncCrawler
Asynchronous parallel wikipedia crawler, using the Java AsyncHttpClient

```bash
./mill -i TestCrawler.scala
```

## Upstream Example: [13.4 - RecursiveCrawler](https://github.com/handsonscala/handsonscala/tree/v2/examples/13.4%20-%20RecursiveCrawler):
Diff:
```diff
diff --git a/13.4 - RecursiveCrawler/Crawler.scala b/13.5 - AsyncCrawler/Crawler.scala
index dcf3ffd..fa0195a 100644
--- a/13.4 - RecursiveCrawler/Crawler.scala	
+++ b/13.5 - AsyncCrawler/Crawler.scala	
@@ -1,14 +1,18 @@
-//| moduleDeps: [FetchLinks.scala]
-import scala.concurrent.*, duration.Duration.Inf, java.util.concurrent.Executors
+//| moduleDeps: [FetchLinksAsync.scala]
+import scala.concurrent.*, java.util.concurrent.Executors
+
 val service = Executors.newFixedThreadPool(8)
 given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)
 
-def fetchAllLinksRec(startTitle: String, depth: Int): Set[String] =
-  def rec(current: Set[String], seen: Set[String], recDepth: Int): Set[String] =
-    if recDepth >= depth then seen
+def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] =
+  def rec(current: Set[String], seen: Set[String], recDepth: Int): Future[Set[String]] =
+    if recDepth >= depth then Future.successful(seen)
     else
-      val futures = for title <- current yield Future{ fetchLinks(title) }
-      val nextTitles = futures.map(Await.result(_, Inf)).flatten
+      val futures = for title <- current yield fetchLinksAsync(title)
+      Future.sequence(futures)
+        .map: nextTitleLists =>
+          val nextTitles = nextTitleLists.flatten
           rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
+        .flatten
 
   rec(Set(startTitle), Set(startTitle), 0)
diff --git a/13.4 - RecursiveCrawler/FetchLinks.scala b/13.4 - RecursiveCrawler/FetchLinks.scala
deleted file mode 100644
index 4d2daf2..0000000
--- a/13.4 - RecursiveCrawler/FetchLinks.scala	
+++ /dev/null
@@ -1,15 +0,0 @@
-def fetchLinks(title: String): Seq[String] =
-  val resp = requests.get(
-    "https://en.wikipedia.org/w/api.php",
-    params = Seq(
-      "action" -> "query",
-      "titles" -> title,
-      "prop" -> "links",
-      "format" -> "json"
-    )
-  )
-  for
-    page <- ujson.read(resp)("query")("pages").obj.values.toSeq
-    links <- page.obj.get("links").toSeq
-    link <- links.arr
-  yield link("title").str
diff --git a/13.5 - AsyncCrawler/FetchLinksAsync.scala b/13.5 - AsyncCrawler/FetchLinksAsync.scala
new file mode 100644
index 0000000..ac8efaf
--- /dev/null
+++ b/13.5 - AsyncCrawler/FetchLinksAsync.scala	
@@ -0,0 +1,20 @@
+//| mvnDeps:
+//| - org.asynchttpclient:async-http-client:2.5.2
+import scala.concurrent.*
+
+val asyncHttpClient = org.asynchttpclient.Dsl.asyncHttpClient()
+
+def fetchLinksAsync(title: String)(using ec: ExecutionContext): Future[Seq[String]] =
+  val p = Promise[String]
+  val listenableFut = asyncHttpClient.prepareGet("https://en.wikipedia.org/w/api.php")
+    .addQueryParam("action", "query").addQueryParam("titles", title)
+    .addQueryParam("prop", "links").addQueryParam("format", "json")
+    .execute()
+
+  listenableFut.addListener(() => p.success(listenableFut.get().getResponseBody), null)
+  p.future.map: responseBody =>
+    for
+      page <- ujson.read(responseBody)("query")("pages").obj.values.toSeq
+      links <- page.obj.get("links").toSeq
+      link <- links.arr
+    yield link("title").str
diff --git a/13.4 - RecursiveCrawler/TestCrawler.scala b/13.5 - AsyncCrawler/TestCrawler.scala
index 214d958..40a5736 100644
--- a/13.4 - RecursiveCrawler/TestCrawler.scala	
+++ b/13.5 - AsyncCrawler/TestCrawler.scala	
@@ -1,10 +1,11 @@
-//| moduleDeps: [Crawler.scala, FetchLinks.scala]
-
+//| moduleDeps: [Crawler.scala, FetchLinksAsync.scala]
 def main() =
-  val depth0Results = pprint.log(fetchAllLinksRec("Singapore", 0))
-  val depth1Results = pprint.log(fetchAllLinksRec("Singapore", 1))
-  val depth2Results = pprint.log(fetchAllLinksRec("Singapore", 2))
-  val depth3Results = pprint.log(fetchAllLinksRec("Singapore", 3))
+  import scala.concurrent._, duration.Duration.Inf
+
+  val depth0Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 0), Inf): Set[String])
+  val depth1Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 1), Inf): Set[String])
+  val depth2Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 2), Inf): Set[String])
+  val depth3Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 3), Inf): Set[String])
 
   pprint.log(depth0Results.size)
   pprint.log(depth1Results.size)
@@ -29,11 +30,10 @@ def main() =
         ".sg"
       )
     )
-
     assert(depth1Results.subsetOf(depth2Results))
     assert(depth1Results.size < depth2Results.size)
-
     assert(depth2Results.subsetOf(depth3Results))
     assert(depth2Results.size < depth3Results.size)
   finally
     service.shutdown()
+    asyncHttpClient.close()
```
## Downstream Examples

- [13.8 - AsyncCrawlerThrottled](https://github.com/handsonscala/handsonscala/tree/v2/examples/13.8%20-%20AsyncCrawlerThrottled)