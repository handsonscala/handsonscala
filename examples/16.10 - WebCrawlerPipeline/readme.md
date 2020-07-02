# Example 16.10 - WebCrawlerPipeline
A concurrent actor-based web crawler that streams the crawled pages to disk

```bash
amm TestWebCrawler.sc
```

## Upstream Example: [16.9 - WebCrawler](https://github.com/handsonscala/handsonscala/tree/v1/examples/16.9%20-%20WebCrawler):
Diff:
```diff
diff --git a/16.9 - WebCrawler/TestWebCrawler.sc b/16.10 - WebCrawlerPipeline/TestWebCrawler.sc
index 1c3024b..ac06ca7 100644
--- a/16.9 - WebCrawler/TestWebCrawler.sc	
+++ b/16.10 - WebCrawlerPipeline/TestWebCrawler.sc	
@@ -34,3 +34,5 @@ assert(depth1Results.size < depth2Results.size)
 
 assert(depth2Results.subsetOf(depth3Results))
 assert(depth2Results.size < depth3Results.size)
+
+assert(os.read(os.pwd / "log.txt").contains("Singapore"))
diff --git a/16.9 - WebCrawler/WebCrawler.sc b/16.10 - WebCrawlerPipeline/WebCrawler.sc
index e91b3ae..bf6f042 100644
--- a/16.9 - WebCrawler/WebCrawler.sc	
+++ b/16.10 - WebCrawlerPipeline/WebCrawler.sc	
@@ -6,7 +6,9 @@ sealed trait Msg
 case class Start(title: String) extends Msg
 case class Fetch(titles: Seq[String], depth: Int) extends Msg
 
-class Crawler(maxDepth: Int, complete: Promise[Set[String]])
+class Crawler(maxDepth: Int,
+              complete: Promise[Set[String]],
+              downstream: castor.Actor[String])
              (implicit cc: castor.Context) extends castor.SimpleActor[Msg] {
   var seen = Set.empty[String]
   var outstanding = 0
@@ -19,6 +21,7 @@ class Crawler(maxDepth: Int, complete: Promise[Set[String]])
   def handle(titles: Seq[String], depth: Int) = {
     for(title <- titles if !seen.contains(title)) {
       if (depth < maxDepth) {
+        downstream.send(title)
         outstanding += 1
         this.sendAsync(fetchLinksAsync(title).map(Fetch(_, depth + 1)))
       }
@@ -29,11 +32,18 @@ class Crawler(maxDepth: Int, complete: Promise[Set[String]])
   }
 }
 
+class DiskActor(logPath: os.Path)
+               (implicit cc: castor.Context) extends castor.SimpleActor[String]{
+
+  def run(s: String) = os.write.append(logPath, s + "\n", createFolders = true)
+}
+
 def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] = {
 
   val complete = Promise[Set[String]]
   implicit val cc = new castor.Context.Test()
-  val crawler = new Crawler(depth, complete)
+  val diskActor = new DiskActor(os.pwd / "log.txt")
+  val crawler = new Crawler(depth, complete, diskActor)
   crawler.send(Start(startTitle))
   complete.future
 }
```
