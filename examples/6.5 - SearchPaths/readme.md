# Example 6.5 - SearchPaths
Breadth-first-search implemention that keeps track of the shortest paths to
every node

```bash
amm TestSearch.sc
```

## Upstream Example: [6.4 - Search](https://github.com/handsonscala/handsonscala/tree/v1/examples/6.4%20-%20Search):
Diff:
```diff
diff --git a/6.4 - Search/Search.sc b/6.5 - SearchPaths/Search.sc
index 3c22173..2a87e04 100644
--- a/6.4 - Search/Search.sc	
+++ b/6.5 - SearchPaths/Search.sc	
@@ -1,12 +1,18 @@
-def search[T](start: T, graph: Map[T, Seq[T]]): Set[T] = {
-  val seen = collection.mutable.Set(start)
-  val queue = collection.mutable.ArrayDeque(start)
+def searchPaths[T](start: T, graph: Map[T, Seq[T]]): Map[T, List[T]] = {
+  val seen = collection.mutable.Map(start -> List(start))
+  val queue = collection.mutable.ArrayDeque(start -> List(start))
   while (queue.nonEmpty) {
-    val current = queue.removeHead()
+    val (current, path) = queue.removeHead()
     for (next <- graph(current) if !seen.contains(next)) {
-      seen.add(next)
-      queue.append(next)
+      val newPath = next :: path
+      seen(next) = newPath
+      queue.append((next, newPath))
     }
   }
-  seen.to(Set)
+  seen.toMap
+}
+
+def shortestPath[T](start: T, dest: T, graph: Map[T, Seq[T]]): Seq[T] = {
+  val shortestReversedPaths = searchPaths(start, graph)
+  shortestReversedPaths(dest).reverse
 }
diff --git a/6.4 - Search/TestSearch.sc b/6.5 - SearchPaths/TestSearch.sc
index 3b3b906..bcc92c1 100644
--- a/6.4 - Search/TestSearch.sc	
+++ b/6.5 - SearchPaths/TestSearch.sc	
@@ -1,23 +1,9 @@
 import $file.Search, Search._
-
-assert(
-  pprint.log(
-    search(
-      start = "c",
-      graph = Map(
-        "a" -> Seq("b", "c"),
-        "b" -> Seq("a"),
-        "c" -> Seq("b")
-      )
-    )
-  ) ==
-    Set("a", "b", "c")
-)
-
 assert(
   pprint.log(
-    search(
+    shortestPath(
       start = "a",
+      dest = "d",
       graph = Map(
         "a" -> Seq("b", "c"),
         "b" -> Seq("c", "d"),
@@ -25,14 +11,14 @@ assert(
         "d" -> Seq()
       )
     )
-  ) ==
-    Set("a", "b", "c", "d")
+  ) == List("a", "b", "d")
 )
 
 assert(
   pprint.log(
-    search(
-      start = "c",
+    shortestPath(
+      start = "a",
+      dest = "c",
       graph = Map(
         "a" -> Seq("b", "c"),
         "b" -> Seq("c", "d"),
@@ -40,6 +26,5 @@ assert(
         "d" -> Seq()
       )
     )
-  ) ==
-    Set("c", "d")
+  ) == List("a", "c")
 )
```
## Downstream Examples

- [6.8 - DepthSearchPaths](https://github.com/handsonscala/handsonscala/tree/v1/examples/6.8%20-%20DepthSearchPaths)