# Example 13.9 - AsyncThrottledScrapingDocs
Asynchronous parallel MDN scraper that limits the number of open requests

```bash
./mill -i TestScrapingDocs.scala
```

## Upstream Example: [13.6 - ParallelScrapingDocs](https://github.com/handsonscala/handsonscala/tree/v2/examples/13.6%20-%20ParallelScrapingDocs):
Diff:
```diff
diff --git a/13.6 - ParallelScrapingDocs/ScrapingDocs.scala b/13.9 - AsyncThrottledScrapingDocs/ScrapingDocs.scala
index 463baa5..d2306bb 100644
--- a/13.6 - ParallelScrapingDocs/ScrapingDocs.scala	
+++ b/13.9 - AsyncThrottledScrapingDocs/ScrapingDocs.scala	
@@ -1,18 +1,30 @@
 //| mvnDeps:
 //| - org.jsoup:jsoup:1.21.2
+//| - org.asynchttpclient:async-http-client:2.5.2
 import org.jsoup.*
-import scala.jdk.CollectionConverters.*
 import scala.concurrent.*, duration.Duration.Inf, java.util.concurrent.Executors
 val service = Executors.newFixedThreadPool(8)
 given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)
+val asyncHttpClient = org.asynchttpclient.Dsl.asyncHttpClient()
+
+def fetchPageAsync(url: String): Future[String] =
+  val p = Promise[org.asynchttpclient.Response]
+  val listenableFut = asyncHttpClient.prepareGet(url).execute()
+  listenableFut.addListener(() => p.success(listenableFut.get()), null)
+  p.future.map(_.getResponseBody)
+
+import scala.jdk.CollectionConverters.*
 
-val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
-val links = indexDoc.select("h2#interfaces").nextAll.select("div.index a").asScala
-val linkData = links.map(link => (link.attr("href"), link.attr("title"), link.text))
+def fetchArticles() =
+  val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
+  val links = indexDoc.select("h2#interfaces").nextAll.select("div.index a").asScala
+  val linkData = links.map(link => (link.attr("href"), link.attr("title"), link.text))
 
-val articlesFutures = for (url, tooltip, name) <- linkData yield Future:
-  println("Scraping " + name)
-  val doc = Jsoup.connect("https://developer.mozilla.org" + url).get()
+  val articleGroups = for (group, i) <- linkData.grouped(16).zipWithIndex yield
+    println("Scraping group " + i)
+    val futures = for (url, tooltip, name) <- group yield
+      fetchPageAsync("https://developer.mozilla.org" + url).map: txt =>
+        val doc = Jsoup.parse(txt)
         val summary = doc.select("article#wikiArticle > p").asScala.headOption match
           case Some(n) => n.text
           case None => ""
@@ -30,6 +42,7 @@ val articlesFutures = for (url, tooltip, name) <- linkData yield Future:
 
         (url, tooltip, name, summary, methodsAndProperties)
 
-val articles = articlesFutures.map(Await.result(_, Inf))
+    futures.map(Await.result(_, Inf))
+  articleGroups.flatten.toList
 
-def main() = pprint.log(articles)
+lazy val articles = fetchArticles()
diff --git a/13.6 - ParallelScrapingDocs/TestScrapingDocs.scala b/13.9 - AsyncThrottledScrapingDocs/TestScrapingDocs.scala
index 98191cf..db03477 100644
--- a/13.6 - ParallelScrapingDocs/TestScrapingDocs.scala	
+++ b/13.9 - AsyncThrottledScrapingDocs/TestScrapingDocs.scala	
@@ -10,3 +10,4 @@ def main() =
     assert(1000 < articles.length && articles.length < 1100)
   finally
     service.shutdown()
+    asyncHttpClient.close()
```
