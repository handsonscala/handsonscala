# Example 16.11 - WebCrawlerThrottled
A concurrent actor-based web crawler that limits the number of open connections

```bash
./mill -i TestWebCrawler.scala
```

## Upstream Example: [16.9 - WebCrawler](https://github.com/handsonscala/handsonscala/tree/v2/examples/16.9%20-%20WebCrawler):
Diff:
```diff
diff --git a/16.9 - WebCrawler/TestWebCrawler.scala b/16.11 - WebCrawlerThrottled/TestWebCrawler.scala
index f548577..d05ab5b 100644
--- a/16.9 - WebCrawler/TestWebCrawler.scala	
+++ b/16.11 - WebCrawlerThrottled/TestWebCrawler.scala	
@@ -2,10 +2,10 @@
 def main() =
   import scala.concurrent.*, duration.Duration.Inf
 
-  val depth0Results = Await.result(fetchAllLinksAsync("Singapore", 0), Inf)
-  val depth1Results = Await.result(fetchAllLinksAsync("Singapore", 1), Inf)
-  val depth2Results = Await.result(fetchAllLinksAsync("Singapore", 2), Inf)
-  val depth3Results = Await.result(fetchAllLinksAsync("Singapore", 3), Inf)
+  val depth0Results = Await.result(fetchAllLinksAsync("Singapore", 0, 16), Inf)
+  val depth1Results = Await.result(fetchAllLinksAsync("Singapore", 1, 16), Inf)
+  val depth2Results = Await.result(fetchAllLinksAsync("Singapore", 2, 16), Inf)
+  val depth3Results = Await.result(fetchAllLinksAsync("Singapore", 3, 16), Inf)
 
   val depth0ResultsAsSeq = pprint.log(depth0Results.toSeq)
   val depth1ResultsAsSeq = pprint.log(depth1Results.toSeq)
diff --git a/16.9 - WebCrawler/WebCrawler.scala b/16.11 - WebCrawlerThrottled/WebCrawler.scala
index 5ca822e..ca246de 100644
--- a/16.9 - WebCrawler/WebCrawler.scala	
+++ b/16.11 - WebCrawlerThrottled/WebCrawler.scala	
@@ -1,16 +1,16 @@
 //| moduleDeps: [FetchLinksAsync.scala]
 //| mvnDeps:
 //| - com.lihaoyi::castor:0.3.0
-
 import scala.concurrent.*
 
 enum Msg:
   case Start(title: String)
   case Fetch(titles: Seq[String], depth: Int)
 
-class Crawler(maxDepth: Int, complete: Promise[Set[String]])
+class Crawler(maxDepth: Int, complete: Promise[Set[String]], maxConcurrency: Int)
              (using cc: castor.Context) extends castor.SimpleActor[Msg]:
   var seen = Set.empty[String]
+  val buffered = collection.mutable.ArrayDeque.empty[(String, Int)]
   var outstanding = 0
 
   def run(msg: Msg) = msg match
@@ -20,19 +20,28 @@ class Crawler(maxDepth: Int, complete: Promise[Set[String]])
       handle(titles, depth)
 
   def handle(titles: Seq[String], depth: Int) =
+    while buffered.nonEmpty && outstanding < maxConcurrency do
+      val (bufferedTitle, bufferedDepth) = buffered.removeHead()
+      fetch(bufferedTitle, bufferedDepth)
+
     for title <- titles if !seen.contains(title) do
       if depth < maxDepth then
-        outstanding += 1
-        this.sendAsync(fetchLinksAsync(title).map(Msg.Fetch(_, depth + 1)))
+        if outstanding < maxConcurrency then fetch(title, depth)
+        else buffered.append(title -> depth)
 
       pprint.log(title)
       seen += title
 
+    pprint.log((buffered.size, seen.size))
     if outstanding == 0 then complete.success(seen)
 
-def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] =
+  def fetch(title: String, depth: Int) =
+    outstanding += 1
+    this.sendAsync(fetchLinksAsync(title).map(Msg.Fetch(_, depth + 1)))
+
+def fetchAllLinksAsync(startTitle: String, depth: Int, maxConcurrency: Int): Future[Set[String]] =
   val complete = Promise[Set[String]]
   given cc: castor.Context.Test()
-  val crawler = new Crawler(depth, complete)
+  val crawler = Crawler(depth, complete, maxConcurrency)
   crawler.send(Msg.Start(startTitle))
   complete.future
```
