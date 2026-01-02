# Example 6.5 - SearchPaths
Breadth-first-search implemention that keeps track of the shortest paths to
every node

```bash
./mill -i TestShortest.scala
```

## Upstream Example: [6.4 - Search](https://github.com/handsonscala/handsonscala/tree/v2/examples/6.4%20-%20Search):
Diff:
```diff
diff --git a/6.4 - Search/Search.scala b/6.5 - SearchPaths/Search.scala
index 5b912d5..58bc02b 100644
--- a/6.4 - Search/Search.scala	
+++ b/6.5 - SearchPaths/Search.scala	
@@ -1,11 +1,12 @@
-def search[T](start: T, graph: Map[T, Seq[T]]): Set[T] =
-  val seen = collection.mutable.Set(start)
-  val queue = collection.mutable.ArrayDeque(start)
+def searchPaths[T](start: T, graph: Map[T, Seq[T]]): Map[T, List[T]] =
+  val seen = collection.mutable.Map(start -> List(start))
+  val queue = collection.mutable.ArrayDeque(start -> List(start))
 
   while queue.nonEmpty do
-    val current = queue.removeHead()
+    val (current, path) = queue.removeHead()
     for next <- graph(current) if !seen.contains(next) do
-      seen.add(next)
-      queue.append(next)
+      val newPath = next :: path
+      seen(next) = newPath
+      queue.append((next, newPath))
 
-  seen.to(Set)
+  seen.toMap
diff --git a/6.5 - SearchPaths/Shortest.scala b/6.5 - SearchPaths/Shortest.scala
new file mode 100644
index 0000000..640c13f
--- /dev/null
+++ b/6.5 - SearchPaths/Shortest.scala	
@@ -0,0 +1,4 @@
+//| moduleDeps: [Search.scala]
+def shortestPath[T](start: T, dest: T, graph: Map[T, Seq[T]]): Seq[T] =
+  val shortestReversedPaths = searchPaths(start, graph)
+  shortestReversedPaths(dest).reverse
diff --git a/6.4 - Search/TestSearch.scala b/6.5 - SearchPaths/TestShortest.scala
similarity index 51%
rename from 6.4 - Search/TestSearch.scala
rename to 6.5 - SearchPaths/TestShortest.scala
index ed820bd..383afbe 100644
--- a/6.4 - Search/TestSearch.scala	
+++ b/6.5 - SearchPaths/TestShortest.scala	
@@ -1,24 +1,11 @@
-//| moduleDeps: [Search.scala]
+//| moduleDeps: [Shortest.scala]
 
 def main() =
   assert(
     pprint.log(
-      search(
-        start = "c",
-        graph = Map(
-          "a" -> Seq("b", "c"),
-          "b" -> Seq("a"),
-          "c" -> Seq("b")
-        )
-      )
-    ) ==
-      Set("a", "b", "c")
-  )
-
-  assert(
-    pprint.log(
-      search(
+      shortestPath(
         start = "a",
+        dest = "d",
         graph = Map(
           "a" -> Seq("b", "c"),
           "b" -> Seq("c", "d"),
@@ -26,14 +13,14 @@ def main() =
           "d" -> Seq()
         )
       )
-    ) ==
-      Set("a", "b", "c", "d")
+    ) == List("a", "b", "d")
   )
 
   assert(
     pprint.log(
-      search(
-        start = "c",
+      shortestPath(
+        start = "a",
+        dest = "c",
         graph = Map(
           "a" -> Seq("b", "c"),
           "b" -> Seq("c", "d"),
@@ -41,6 +28,5 @@ def main() =
           "d" -> Seq()
         )
       )
-    ) ==
-      Set("c", "d")
+    ) == List("a", "c")
   )
```
## Downstream Examples

- [6.8 - DepthSearchPaths](https://github.com/handsonscala/handsonscala/tree/v2/examples/6.8%20-%20DepthSearchPaths)