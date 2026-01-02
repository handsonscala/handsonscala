# Example 6.10 - TrieMap
Using a trie to implement a `Map[String, T]`-like data structure

```bash
./mill -i TestTrie.scala
./mill -i TestTrie2.scala
```

## Upstream Example: [6.3 - Trie](https://github.com/handsonscala/handsonscala/tree/v2/examples/6.3%20-%20Trie):
Diff:
```diff
diff --git a/6.3 - Trie/TestTrie.scala b/6.10 - TrieMap/TestTrie.scala
index 7e2ee72..4e13358 100644
--- a/6.3 - Trie/TestTrie.scala	
+++ b/6.10 - TrieMap/TestTrie.scala	
@@ -1,12 +1,12 @@
 //| moduleDeps: [Trie.scala]
 
 def main() =
-  val t = Trie()
+  val t = Trie[Int]()
 
-  t.add("mango")
-  t.add("mandarin")
-  t.add("map")
-  t.add("man")
+  t.add("mango", 1337)
+  t.add("mandarin", 31337)
+  t.add("map", 37)
+  t.add("man", 7)
 
   assert(pprint.log(t.contains("mango")) == true)
 
@@ -18,14 +18,14 @@ def main() =
 
   assert(pprint.log(t.contains("mandarine")) == false)
 
-  assert(pprint.log(t.prefixesMatchingString("mangosteen")) == Set("man", "mango"))
+  assert(pprint.log(t.prefixesMatchingString("mangosteen")) == Map("man" -> 7, "mango" -> 1337))
 
-  assert(pprint.log(t.stringsMatchingPrefix("man")) == Set("man", "mandarin", "mango"))
+  assert(pprint.log(t.stringsMatchingPrefix("man")) == Map("man" -> 7, "mandarin" -> 31337, "mango" -> 1337))
 
-  assert(pprint.log(t.stringsMatchingPrefix("ma")) == Set("map", "man", "mandarin", "mango"))
+  assert(pprint.log(t.stringsMatchingPrefix("ma")) == Map("map" -> 37, "man" -> 7, "mandarin" -> 31337, "mango" -> 1337))
 
-  assert(pprint.log(t.stringsMatchingPrefix("map")) == Set("map"))
+  assert(pprint.log(t.stringsMatchingPrefix("map")) == Map("map" -> 37))
 
-  assert(pprint.log(t.stringsMatchingPrefix("mand")) == Set("mandarin"))
+  assert(pprint.log(t.stringsMatchingPrefix("mand")) == Map("mandarin" -> 31337))
 
-  assert(pprint.log(t.stringsMatchingPrefix("mando")) == Set())
+  assert(pprint.log(t.stringsMatchingPrefix("mando")) == Map())
diff --git a/6.10 - TrieMap/TestTrie2.scala b/6.10 - TrieMap/TestTrie2.scala
new file mode 100644
index 0000000..6a15add
--- /dev/null
+++ b/6.10 - TrieMap/TestTrie2.scala	
@@ -0,0 +1,8 @@
+//| moduleDeps: [Trie.scala]
+def main() =
+  val t = Trie[Int]()
+  t.add("mango", 1337); t.add("mandarin", 31337); t.add("map", 37); t.add("man", 7)
+
+  assert(t.get("mango") == Some(1337))
+  assert(t.prefixesMatchingString("mangosteen") == Map("man" -> 7, "mango" -> 1337))
+  assert(t.stringsMatchingPrefix("mand") == Map("mandarin" -> 31337))
diff --git a/6.3 - Trie/Trie.scala b/6.10 - TrieMap/Trie.scala
index fec6857..77bd011 100644
--- a/6.3 - Trie/Trie.scala	
+++ b/6.10 - TrieMap/Trie.scala	
@@ -1,42 +1,44 @@
-class Trie():
+class Trie[T]():
   import collection.mutable
-  class Node(var hasValue: Boolean, val children: mutable.Map[Char, Node] = mutable.Map())
+  class Node(var value: Option[T], val children: mutable.Map[Char, Node] = mutable.Map())
 
-  val root = Node(false)
+  val root = Node(None)
 
-  def add(s: String) =
+  def add(s: String, v: T) =
     var current = root
-    for c <- s do current = current.children.getOrElseUpdate(c, Node(false))
-    current.hasValue = true
+    for c <- s do current = current.children.getOrElseUpdate(c, Node(None))
+    current.value = Some(v)
 
-  def contains(s: String): Boolean =
+  def contains(s: String): Boolean = get(s).isDefined
+
+  def get(s: String): Option[T] =
     var current = Option(root)
     for c <- s if current.nonEmpty do current = current.get.children.get(c)
-    current.exists(_.hasValue)
+    current.flatMap(_.value)
 
-  def prefixesMatchingString0(s: String): Set[Int] =
+  def prefixesMatchingString0(s: String): Map[Int, T] =
     var current = Option(root)
-    val output = Set.newBuilder[Int]
+    val output = Map.newBuilder[Int, T]
 
     for (c, i) <- s.zipWithIndex if current.nonEmpty do
-      if current.get.hasValue then output += i
+      for v <- current.get.value do output += (i -> v)
       current = current.get.children.get(c)
 
-    if current.exists(_.hasValue) then output += s.length
+    for c <- current; v <- c.value do output += (s.length -> v)
     output.result()
 
-  def prefixesMatchingString(s: String): Set[String] =
-    prefixesMatchingString0(s).map(s.substring(0, _))
+  def prefixesMatchingString(s: String): Map[String, T] =
+    prefixesMatchingString0(s).map { case (k, v) => (s.substring(0, k), v)}
 
-  def stringsMatchingPrefix(s: String): Set[String] =
+  def stringsMatchingPrefix(s: String): Map[String, T] =
     var current = Option(root)
     for c <- s if current.nonEmpty do current = current.get.children.get(c) // initial walk
 
-    if current.isEmpty then Set()
+    if current.isEmpty then Map()
     else
-      val output = Set.newBuilder[String]
+      val output = Map.newBuilder[String, T]
       def recurse(current: Node, path: List[Char]): Unit =
-        if current.hasValue then output += (s + path.reverse.mkString)
+        for v <- current.value do output += (s + path.reverse.mkString -> v)
         for (c, n) <- current.children do recurse(n, c :: path)
 
       recurse(current.get, Nil) // recursive walk
```
