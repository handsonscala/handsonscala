# Example 13.9 - AsyncThrottledScrapingDocs
Asynchronous parallel MDN scraper that limits the number of open requests

```bash
amm --class-based TestScrapingDocs.sc
```

## Upstream Example: [13.6 - ParallelScrapingDocs](https://github.com/handsonscala/handsonscala/tree/v1/examples/13.6%20-%20ParallelScrapingDocs):
Diff:
```diff
diff --git a/13.6 - ParallelScrapingDocs/ScrapingDocs.sc b/13.9 - AsyncThrottledScrapingDocs/ScrapingDocs.sc
index 1549bb4..7f43e6f 100644
--- a/13.6 - ParallelScrapingDocs/ScrapingDocs.sc	
+++ b/13.9 - AsyncThrottledScrapingDocs/ScrapingDocs.sc	
@@ -1,14 +1,25 @@
 import $ivy.`org.jsoup:jsoup:1.13.1`, org.jsoup._
-import collection.JavaConverters._
+import $ivy.`org.asynchttpclient:async-http-client:2.5.2`
 import scala.concurrent._, duration.Duration.Inf, java.util.concurrent.Executors
 implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
+val asyncHttpClient = org.asynchttpclient.Dsl.asyncHttpClient()
+
+def fetchPageAsync(url: String): Future[String] = {
+  val p = Promise[org.asynchttpclient.Response]
+  val listenableFut = asyncHttpClient.prepareGet(url).execute()
+  listenableFut.addListener(() => p.success(listenableFut.get()), null)
+  p.future.map(_.getResponseBody)
+}
 
+import collection.JavaConverters._
 val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
 val links = indexDoc.select("h2#Interfaces").nextAll.select("div.index a").asScala
 val linkData = links.map(link => (link.attr("href"), link.attr("title"), link.text))
-val articlesFutures = for ((url, tooltip, name) <- linkData) yield Future{
-  println("Scraping " + name)
-  val doc = Jsoup.connect("https://developer.mozilla.org" + url).get()
+val articleGroups = for ((group, i) <- linkData.grouped(16).zipWithIndex) yield {
+  println("Scraping group " + i)
+  val futures = for((url, tooltip, name) <- group) yield {
+    fetchPageAsync("https://developer.mozilla.org" + url).map { txt =>
+      val doc = Jsoup.parse(txt)
       val summary = doc.select("article#wikiArticle > p").asScala.headOption match {
         case Some(n) => n.text; case None => ""
       }
@@ -17,6 +28,9 @@ val articlesFutures = for ((url, tooltip, name) <- linkData) yield Future{
         .asScala
         .map(el => (el.text, el.nextElementSibling match {case null => ""; case x => x.text}))
       (url, tooltip, name, summary, methodsAndProperties)
+    }
+  }
+  futures.map(Await.result(_, Inf))
 }
 
-val articles = articlesFutures.map(Await.result(_, Inf))
+val articles = articleGroups.flatten.toList
```
