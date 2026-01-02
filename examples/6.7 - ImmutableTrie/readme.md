# Example 6.7 - ImmutableTrie
Immutable trie implementation

```bash
./mill -i TestTrie.scala
```

## Upstream Example: [6.3 - Trie](https://github.com/handsonscala/handsonscala/tree/v2/examples/6.3%20-%20Trie):
Diff:
```diff
diff --git a/6.3 - Trie/TestTrie.scala b/6.7 - ImmutableTrie/TestTrie.scala
index 7e2ee72..422ffb4 100644
--- a/6.3 - Trie/TestTrie.scala	
+++ b/6.7 - ImmutableTrie/TestTrie.scala	
@@ -1,12 +1,7 @@
 //| moduleDeps: [Trie.scala]
 
 def main() =
-  val t = Trie()
-
-  t.add("mango")
-  t.add("mandarin")
-  t.add("map")
-  t.add("man")
+  val t = new ImmutableTrie(Seq("mango", "mandarin", "map", "man"))
 
   assert(pprint.log(t.contains("mango")) == true)
 
diff --git a/6.3 - Trie/Trie.scala b/6.7 - ImmutableTrie/Trie.scala
index fec6857..ccb71aa 100644
--- a/6.3 - Trie/Trie.scala	
+++ b/6.7 - ImmutableTrie/Trie.scala	
@@ -1,43 +1,36 @@
-class Trie():
-  import collection.mutable
-  class Node(var hasValue: Boolean, val children: mutable.Map[Char, Node] = mutable.Map())
 
-  val root = Node(false)
+class ImmutableTrie(inputs: Seq[String]):
+  class Node(index: Int, inputs: Seq[String]):
+    val hasValue = inputs.exists(_.length == index)
+    val children =
+      val filteredInputs = inputs.filter(_.length > index)
+      for (childChar, childInputs) <- filteredInputs.groupBy(_.charAt(index))
+      yield (childChar, Node(index + 1, childInputs))
 
-  def add(s: String) =
-    var current = root
-    for c <- s do current = current.children.getOrElseUpdate(c, Node(false))
-    current.hasValue = true
+  val root = Node(0, inputs)
 
   def contains(s: String): Boolean =
     var current = Option(root)
     for c <- s if current.nonEmpty do current = current.get.children.get(c)
     current.exists(_.hasValue)
-
   def prefixesMatchingString0(s: String): Set[Int] =
     var current = Option(root)
     val output = Set.newBuilder[Int]
-
     for (c, i) <- s.zipWithIndex if current.nonEmpty do
       if current.get.hasValue then output += i
       current = current.get.children.get(c)
-
     if current.exists(_.hasValue) then output += s.length
     output.result()
-
   def prefixesMatchingString(s: String): Set[String] =
     prefixesMatchingString0(s).map(s.substring(0, _))
-
   def stringsMatchingPrefix(s: String): Set[String] =
     var current = Option(root)
     for c <- s if current.nonEmpty do current = current.get.children.get(c) // initial walk
-
     if current.isEmpty then Set()
     else
       val output = Set.newBuilder[String]
       def recurse(current: Node, path: List[Char]): Unit =
         if current.hasValue then output += (s + path.reverse.mkString)
         for (c, n) <- current.children do recurse(n, c :: path)
-      
       recurse(current.get, Nil) // recursive walk
       output.result()
```
