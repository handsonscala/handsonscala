# Example 16.10 - WebCrawlerPipeline
A concurrent actor-based web crawler that streams the crawled pages to disk

```bash
./mill -i TestWebCrawler.scala
```

## Upstream Example: [16.9 - WebCrawler](https://github.com/handsonscala/handsonscala/tree/v2/examples/16.9%20-%20WebCrawler):
Diff:
```diff
diff --git a/16.9 - WebCrawler/TestWebCrawler.scala b/16.10 - WebCrawlerPipeline/TestWebCrawler.scala
index f548577..8a28aba 100644
--- a/16.9 - WebCrawler/TestWebCrawler.scala	
+++ b/16.10 - WebCrawlerPipeline/TestWebCrawler.scala	
@@ -41,4 +41,6 @@ def main() =
   assert(depth2Results.subsetOf(depth3Results))
   assert(depth2Results.size < depth3Results.size)
 
+  assert(os.read(os.pwd / "log.txt").contains("Singapore"))
+
   asyncHttpClient.close()
diff --git a/16.9 - WebCrawler/WebCrawler.scala b/16.10 - WebCrawlerPipeline/WebCrawler.scala
index 5ca822e..187a928 100644
--- a/16.9 - WebCrawler/WebCrawler.scala	
+++ b/16.10 - WebCrawlerPipeline/WebCrawler.scala	
@@ -1,14 +1,15 @@
 //| moduleDeps: [FetchLinksAsync.scala]
 //| mvnDeps:
 //| - com.lihaoyi::castor:0.3.0
-
 import scala.concurrent.*
 
 enum Msg:
   case Start(title: String)
   case Fetch(titles: Seq[String], depth: Int)
 
-class Crawler(maxDepth: Int, complete: Promise[Set[String]])
+class Crawler(maxDepth: Int,
+              complete: Promise[Set[String]],
+              downstream: castor.Actor[String])
              (using cc: castor.Context) extends castor.SimpleActor[Msg]:
   var seen = Set.empty[String]
   var outstanding = 0
@@ -22,6 +23,7 @@ class Crawler(maxDepth: Int, complete: Promise[Set[String]])
   def handle(titles: Seq[String], depth: Int) =
     for title <- titles if !seen.contains(title) do
       if depth < maxDepth then
+        downstream.send(title)
         outstanding += 1
         this.sendAsync(fetchLinksAsync(title).map(Msg.Fetch(_, depth + 1)))
      
@@ -30,9 +32,15 @@ class Crawler(maxDepth: Int, complete: Promise[Set[String]])
     
     if outstanding == 0 then complete.success(seen)
 
+class DiskActor(logPath: os.Path)
+               (using cc: castor.Context) extends castor.SimpleActor[String]:
+
+  def run(s: String) = os.write.append(logPath, s + "\n", createFolders = true)
+
 def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] =
   val complete = Promise[Set[String]]
   given cc: castor.Context.Test()
-  val crawler = new Crawler(depth, complete)
+  val diskActor = DiskActor(os.pwd / "log.txt")
+  val crawler = Crawler(depth, complete, diskActor)
   crawler.send(Msg.Start(startTitle))
   complete.future
```
