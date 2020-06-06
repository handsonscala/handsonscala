# Example 6.8 - DepthSearchPaths
Depth-first search implementation that keeps track of shortest paths

```bash
amm TestSearch.sc
```

## Upstream Example: [6.5 - SearchPaths](https://github.com/handsonscala/handsonscala/tree/v1/examples/6.5%20-%20SearchPaths):
Diff:
```diff
diff --git a/6.5 - SearchPaths/Search.sc b/6.8 - DepthSearchPaths/Search.sc
index 2a87e04..04f9fbc 100644
--- a/6.5 - SearchPaths/Search.sc	
+++ b/6.8 - DepthSearchPaths/Search.sc	
@@ -1,18 +1,23 @@
-def searchPaths[T](start: T, graph: Map[T, Seq[T]]): Map[T, List[T]] = {
+def depthSearchPaths[T](start: T, graph: Map[T, Seq[T]]): Map[T, List[T]] = {
   val seen = collection.mutable.Map(start -> List(start))
-  val queue = collection.mutable.ArrayDeque(start -> List(start))
+  val pathLengths = collection.mutable.Map(start -> 0)
+  val queue = collection.mutable.ArrayDeque((start, List(start), 0))
   while (queue.nonEmpty) {
-    val (current, path) = queue.removeHead()
-    for (next <- graph(current) if !seen.contains(next)) {
+    val (current, path, pathLength) = queue.removeLast()
+    for {
+      next <- graph(current)
+      if !seen.contains(next) && !pathLengths.get(next).exists(_ <= pathLength + 1)
+    } {
       val newPath = next :: path
       seen(next) = newPath
-      queue.append((next, newPath))
+      pathLengths(next) = pathLength + 1
+      queue.append((next, newPath, pathLength + 1))
     }
   }
   seen.toMap
 }
 
 def shortestPath[T](start: T, dest: T, graph: Map[T, Seq[T]]): Seq[T] = {
-  val shortestReversedPaths = searchPaths(start, graph)
+  val shortestReversedPaths = depthSearchPaths(start, graph)
   shortestReversedPaths(dest).reverse
 }
diff --git a/6.5 - SearchPaths/TestSearch.sc b/6.8 - DepthSearchPaths/TestSearch.sc
index bcc92c1..fffa8cf 100644
--- a/6.5 - SearchPaths/TestSearch.sc	
+++ b/6.8 - DepthSearchPaths/TestSearch.sc	
@@ -11,7 +11,7 @@ assert(
         "d" -> Seq()
       )
     )
-  ) == List("a", "b", "d")
+  ) == List("a", "c", "d")
 )
 
 assert(
```
