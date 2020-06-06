# Example 16.11 - WebCrawlerThrottled
A concurrent actor-based web crawler that limits the number of open connections

```bash
amm TestWebCrawler.sc
```

## Upstream Example: [16.9 - WebCrawler](https://github.com/handsonscala/handsonscala/tree/v1/examples/16.9%20-%20WebCrawler):
Diff:
```diff
diff --git a/16.9 - WebCrawler/TestWebCrawler.sc b/16.11 - WebCrawlerThrottled/TestWebCrawler.sc
index 1c3024b..4ce7c25 100644
--- a/16.9 - WebCrawler/TestWebCrawler.sc	
+++ b/16.11 - WebCrawlerThrottled/TestWebCrawler.sc	
@@ -1,10 +1,10 @@
 import $file.WebCrawler, WebCrawler._
 import scala.concurrent._, duration.Duration.Inf
 
-val depth0Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 0), Inf))
-val depth1Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 1), Inf))
-val depth2Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 2), Inf))
-val depth3Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 3), Inf))
+val depth0Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 0, 16), Inf))
+val depth1Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 1, 16), Inf))
+val depth2Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 2, 16), Inf))
+val depth3Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 3, 16), Inf))
 
 pprint.log(depth0Results.size)
 pprint.log(depth1Results.size)
diff --git a/16.9 - WebCrawler/WebCrawler.sc b/16.11 - WebCrawlerThrottled/WebCrawler.sc
index e91b3ae..2492b46 100644
--- a/16.9 - WebCrawler/WebCrawler.sc	
+++ b/16.11 - WebCrawlerThrottled/WebCrawler.sc	
@@ -6,9 +6,10 @@ sealed trait Msg
 case class Start(title: String) extends Msg
 case class Fetch(titles: Seq[String], depth: Int) extends Msg
 
-class Crawler(maxDepth: Int, complete: Promise[Set[String]])
+class Crawler(maxDepth: Int, complete: Promise[Set[String]], maxConcurrency: Int)
              (implicit cc: castor.Context) extends castor.SimpleActor[Msg] {
   var seen = Set.empty[String]
+  val buffered = collection.mutable.ArrayDeque.empty[(String, Int)]
   var outstanding = 0
   def run(msg: Msg) = msg match{
     case Start(title) => handle(Seq(title), 0)
@@ -17,23 +18,35 @@ class Crawler(maxDepth: Int, complete: Promise[Set[String]])
       handle(titles, depth)
   }
   def handle(titles: Seq[String], depth: Int) = {
+    while(buffered.nonEmpty && outstanding < maxConcurrency){
+      val (bufferedTitle, bufferedDepth) = buffered.removeHead()
+      fetch(bufferedTitle, bufferedDepth)
+    }
+
     for(title <- titles if !seen.contains(title)) {
       if (depth < maxDepth) {
-        outstanding += 1
-        this.sendAsync(fetchLinksAsync(title).map(Fetch(_, depth + 1)))
+        if (outstanding < maxConcurrency) fetch(title, depth)
+        else buffered.append(title -> depth)
       }
       pprint.log(title)
       seen += title
     }
+
+    pprint.log((buffered.size, seen.size))
     if (outstanding == 0) complete.success(seen)
   }
+
+  def fetch(title: String, depth: Int) = {
+    outstanding += 1
+    this.sendAsync(fetchLinksAsync(title).map(Fetch(_, depth + 1)))
+  }
 }
 
-def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] = {
+def fetchAllLinksAsync(startTitle: String, depth: Int, maxConcurrency: Int): Future[Set[String]] = {
 
   val complete = Promise[Set[String]]
   implicit val cc = new castor.Context.Test()
-  val crawler = new Crawler(depth, complete)
+  val crawler = new Crawler(depth, complete, maxConcurrency)
   crawler.send(Start(startTitle))
   complete.future
 }
```
