# Example 6.8 - DepthSearchPaths
Depth-first search implementation that keeps track of shortest paths

```bash
./mill -i TestSearch.scala
```

## Upstream Example: [6.5 - SearchPaths](https://github.com/handsonscala/handsonscala/tree/v2/examples/6.5%20-%20SearchPaths):
Diff:
```diff
diff --git a/6.5 - SearchPaths/Search.scala b/6.8 - DepthSearchPaths/Search.scala
index 58bc02b..11a8086 100644
--- a/6.5 - SearchPaths/Search.scala	
+++ b/6.8 - DepthSearchPaths/Search.scala	
@@ -1,12 +1,21 @@
-def searchPaths[T](start: T, graph: Map[T, Seq[T]]): Map[T, List[T]] =
+def depthSearchPaths[T](start: T, graph: Map[T, Seq[T]]): Map[T, List[T]] =
   val seen = collection.mutable.Map(start -> List(start))
-  val queue = collection.mutable.ArrayDeque(start -> List(start))
+  val pathLengths = collection.mutable.Map(start -> 0)
+  val queue = collection.mutable.ArrayDeque((start, List(start), 0))
 
   while queue.nonEmpty do
-    val (current, path) = queue.removeHead()
-    for next <- graph(current) if !seen.contains(next) do
+    val (current, path, pathLength) = queue.removeLast()
+    for
+      next <- graph.getOrElse(current, Nil)
+      if !seen.contains(next) && !pathLengths.get(next).exists(_ <= pathLength + 1)
+    do
       val newPath = next :: path
       seen(next) = newPath
-      queue.append((next, newPath))
+      pathLengths(next) = pathLength + 1
+      queue.append((next, newPath, pathLength + 1))
 
   seen.toMap
+
+def shortestPath[T](start: T, dest: T, graph: Map[T, Seq[T]]): Seq[T] =
+  val shortestReversedPaths = depthSearchPaths(start, graph)
+  shortestReversedPaths(dest).reverse
diff --git a/6.5 - SearchPaths/Shortest.scala b/6.5 - SearchPaths/Shortest.scala
deleted file mode 100644
index 640c13f..0000000
--- a/6.5 - SearchPaths/Shortest.scala	
+++ /dev/null
@@ -1,4 +0,0 @@
-//| moduleDeps: [Search.scala]
-def shortestPath[T](start: T, dest: T, graph: Map[T, Seq[T]]): Seq[T] =
-  val shortestReversedPaths = searchPaths(start, graph)
-  shortestReversedPaths(dest).reverse
diff --git a/6.5 - SearchPaths/TestShortest.scala b/6.8 - DepthSearchPaths/TestSearch.scala
similarity index 89%
rename from 6.5 - SearchPaths/TestShortest.scala
rename to 6.8 - DepthSearchPaths/TestSearch.scala
index 383afbe..5070e8f 100644
--- a/6.5 - SearchPaths/TestShortest.scala	
+++ b/6.8 - DepthSearchPaths/TestSearch.scala	
@@ -1,4 +1,4 @@
-//| moduleDeps: [Shortest.scala]
+//| moduleDeps: [Search.scala]
 
 def main() =
   assert(
@@ -13,7 +13,7 @@ def main() =
           "d" -> Seq()
         )
       )
-    ) == List("a", "b", "d")
+    ) == List("a", "c", "d")
   )
 
   assert(
```
