# Example 13.5 - AsyncCrawler
Asynchronous parallel wikipedia crawler, using the Java AsyncHttpClient

```bash
amm --class-based TestCrawler.sc
```

## Upstream Example: [13.4 - RecursiveCrawler](https://github.com/handsonscala/handsonscala/tree/v1/examples/13.4%20-%20RecursiveCrawler):
Diff:
```diff
diff --git a/13.4 - RecursiveCrawler/Crawler.sc b/13.5 - AsyncCrawler/Crawler.sc
index f4079be..08043e7 100644
--- a/13.4 - RecursiveCrawler/Crawler.sc	
+++ b/13.5 - AsyncCrawler/Crawler.sc	
@@ -1,13 +1,14 @@
-import $file.FetchLinks, FetchLinks._
-import scala.concurrent._, ExecutionContext.Implicits.global, duration.Duration.Inf
-def fetchAllLinksRec(startTitle: String, depth: Int): Set[String] = {
-  def rec(current: Set[String], seen: Set[String], recDepth: Int): Set[String] = {
-    if (recDepth >= depth) seen
+import $file.FetchLinksAsync, FetchLinksAsync._
+import scala.concurrent._, ExecutionContext.Implicits.global
+def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] = {
+  def rec(current: Set[String], seen: Set[String], recDepth: Int): Future[Set[String]] = {
+    if (recDepth >= depth) Future.successful(seen)
     else {
-      val futures = for (title <- current) yield Future{ fetchLinks(title) }
-      val nextTitleLists = futures.map(Await.result(_, Inf))
+      val futures = for (title <- current) yield fetchLinksAsync(title)
+      Future.sequence(futures).map{nextTitleLists =>
         val nextTitles = nextTitleLists.flatten
         rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
+      }.flatten
     }
   }
   rec(Set(startTitle), Set(startTitle), 0)
diff --git a/13.4 - RecursiveCrawler/FetchLinks.sc b/13.4 - RecursiveCrawler/FetchLinks.sc
deleted file mode 100644
index bfe2602..0000000
--- a/13.4 - RecursiveCrawler/FetchLinks.sc	
+++ /dev/null
@@ -1,16 +0,0 @@
-def fetchLinks(title: String): Seq[String] = {
-  val resp = requests.get(
-    "https://en.wikipedia.org/w/api.php",
-    params = Seq(
-      "action" -> "query",
-      "titles" -> title,
-      "prop" -> "links",
-      "format" -> "json"
-    )
-  )
-  for{
-    page <- ujson.read(resp.text())("query")("pages").obj.values.toSeq
-    links <- page.obj.get("links").toSeq
-    link <- links.arr
-  } yield link("title").str
-}
diff --git a/13.5 - AsyncCrawler/FetchLinksAsync.sc b/13.5 - AsyncCrawler/FetchLinksAsync.sc
new file mode 100644
index 0000000..0b03d90
--- /dev/null
+++ b/13.5 - AsyncCrawler/FetchLinksAsync.sc	
@@ -0,0 +1,22 @@
+import $ivy.`org.asynchttpclient:async-http-client:2.5.2`
+import scala.concurrent._, ExecutionContext.Implicits.global
+
+val asyncHttpClient = org.asynchttpclient.Dsl.asyncHttpClient()
+
+def fetchLinksAsync(title: String): Future[Seq[String]] = {
+  val p = Promise[String]
+  val listenableFut = asyncHttpClient.prepareGet("https://en.wikipedia.org/w/api.php")
+    .addQueryParam("action", "query").addQueryParam("titles", title)
+    .addQueryParam("prop", "links").addQueryParam("format", "json")
+    .execute()
+
+  listenableFut.addListener(() => p.success(listenableFut.get().getResponseBody), null)
+  val scalaFut: Future[String] = p.future
+  scalaFut.map{ responseBody =>
+    for{
+      page <- ujson.read(responseBody)("query")("pages").obj.values.toSeq
+      links <- page.obj.get("links").toSeq
+      link <- links.arr
+    } yield link("title").str
+  }
+}
diff --git a/13.4 - RecursiveCrawler/TestCrawler.sc b/13.5 - AsyncCrawler/TestCrawler.sc
index 7522fa7..52e0a01 100644
--- a/13.4 - RecursiveCrawler/TestCrawler.sc	
+++ b/13.5 - AsyncCrawler/TestCrawler.sc	
@@ -1,9 +1,10 @@
 import $file.Crawler, Crawler._
+import scala.concurrent._, duration.Duration.Inf
 
-val depth0Results = pprint.log(fetchAllLinksRec("Singapore", 0))
-val depth1Results = pprint.log(fetchAllLinksRec("Singapore", 1))
-val depth2Results = pprint.log(fetchAllLinksRec("Singapore", 2))
-val depth3Results = pprint.log(fetchAllLinksRec("Singapore", 3))
+val depth0Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 0), Inf))
+val depth1Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 1), Inf))
+val depth2Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 2), Inf))
+val depth3Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 3), Inf))
 
 pprint.log(depth0Results.size)
 pprint.log(depth1Results.size)
```
## Downstream Examples

- [13.8 - AsyncCrawlerThrottled](https://github.com/handsonscala/handsonscala/tree/v1/examples/13.8%20-%20AsyncCrawlerThrottled)