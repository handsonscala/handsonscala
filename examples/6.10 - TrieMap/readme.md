# Example 6.10 - TrieMap
Using a trie to implement a `Map[String, T]`-like data structure

```bash
amm TestTrie.sc
amm --predef Trie.sc TestTrie2.sc
```

## Upstream Example: [6.3 - Trie](https://github.com/handsonscala/handsonscala/tree/v1/examples/6.3%20-%20Trie):
Diff:
```diff
diff --git a/6.3 - Trie/TestTrie.sc b/6.10 - TrieMap/TestTrie.sc
index cc75158..7be4047 100644
--- a/6.3 - Trie/TestTrie.sc	
+++ b/6.10 - TrieMap/TestTrie.sc	
@@ -1,11 +1,11 @@
 import $file.Trie, Trie._
 
-val t = new Trie()
+val t = new Trie[Int]()
 
-t.add("mango")
-t.add("mandarin")
-t.add("map")
-t.add("man")
+t.add("mango", 1337)
+t.add("mandarin", 31337)
+t.add("map", 37)
+t.add("man", 7)
 
 assert(pprint.log(t.contains("mango")) == true)
 
@@ -17,14 +17,14 @@ assert(pprint.log(t.contains("mandarin")) == true)
 
 assert(pprint.log(t.contains("mandarine")) == false)
 
-assert(pprint.log(t.prefixesMatchingString("mangosteen")) == Set("man", "mango"))
+assert(pprint.log(t.prefixesMatchingString("mangosteen")) == Map("man" -> 7, "mango" -> 1337))
 
-assert(pprint.log(t.stringsMatchingPrefix("man")) == Set("man", "mandarin", "mango"))
+assert(pprint.log(t.stringsMatchingPrefix("man")) == Map("man" -> 7, "mandarin" -> 31337, "mango" -> 1337))
 
-assert(pprint.log(t.stringsMatchingPrefix("ma")) == Set("map", "man", "mandarin", "mango"))
+assert(pprint.log(t.stringsMatchingPrefix("ma")) == Map("map" -> 37, "man" -> 7, "mandarin" -> 31337, "mango" -> 1337))
 
-assert(pprint.log(t.stringsMatchingPrefix("map")) == Set("map"))
+assert(pprint.log(t.stringsMatchingPrefix("map")) == Map("map" -> 37))
 
-assert(pprint.log(t.stringsMatchingPrefix("mand")) == Set("mandarin"))
+assert(pprint.log(t.stringsMatchingPrefix("mand")) == Map("mandarin" -> 31337))
 
-assert(pprint.log(t.stringsMatchingPrefix("mando")) == Set())
+assert(pprint.log(t.stringsMatchingPrefix("mando")) == Map())
diff --git a/6.10 - TrieMap/TestTrie2.sc b/6.10 - TrieMap/TestTrie2.sc
new file mode 100644
index 0000000..ebc4597
--- /dev/null
+++ b/6.10 - TrieMap/TestTrie2.sc	
@@ -0,0 +1,5 @@
+val t = new Trie[Int]()
+t.add("mango", 1337); t.add("mandarin", 31337); t.add("map", 37); t.add("man", 7)
+assert(t.get("mango") == Some(1337))
+assert(t.prefixesMatchingString("mangosteen") == Map("man" -> 7, "mango" -> 1337))
+assert(t.stringsMatchingPrefix("mand") == Map("mandarin" -> 31337))
diff --git a/6.3 - Trie/Trie.sc b/6.10 - TrieMap/Trie.sc
index 9325268..d0d094c 100644
--- a/6.3 - Trie/Trie.sc	
+++ b/6.10 - TrieMap/Trie.sc	
@@ -1,38 +1,39 @@
-class Trie() {
-  class Node(var hasValue: Boolean,
+class Trie[T]() {
+  class Node(var value: Option[T],
              val children: collection.mutable.Map[Char, Node] = collection.mutable.Map())
-  val root = new Node(false)
-  def add(s: String) = {
+  val root = new Node(None)
+  def add(s: String, v: T) = {
     var current = root
-    for (c <- s) current = current.children.getOrElseUpdate(c, new Node(false))
-    current.hasValue = true
+    for (c <- s) current = current.children.getOrElseUpdate(c, new Node(None))
+    current.value = Some(v)
   }
-  def contains(s: String): Boolean = {
+  def contains(s: String): Boolean = get(s).isDefined
+  def get(s: String): Option[T] = {
     var current = Option(root)
     for (c <- s if current.nonEmpty) current = current.get.children.get(c)
-    current.exists(_.hasValue)
+    current.flatMap(_.value)
   }
-  def prefixesMatchingString0(s: String): Set[Int] = {
+  def prefixesMatchingString0(s: String): Map[Int, T] = {
     var current = Option(root)
-    val output = Set.newBuilder[Int]
+    val output = Map.newBuilder[Int, T]
     for ((c, i) <- s.zipWithIndex if current.nonEmpty) {
-      if (current.get.hasValue) output += i
+      for (v <- current.get.value) output += (i -> v)
       current = current.get.children.get(c)
     }
-    if (current.exists(_.hasValue)) output += s.length
+    for (c <- current; v <- c.value) output += (s.length -> v)
     output.result()
   }
-  def prefixesMatchingString(s: String): Set[String] = {
-    prefixesMatchingString0(s).map(s.substring(0, _))
+  def prefixesMatchingString(s: String): Map[String, T] = {
+    prefixesMatchingString0(s).map { case (k, v) => (s.substring(0, k), v)}
   }
-  def stringsMatchingPrefix(s: String): Set[String] = {
+  def stringsMatchingPrefix(s: String): Map[String, T] = {
     var current = Option(root)
     for (c <- s if current.nonEmpty) current = current.get.children.get(c) // initial walk
-    if (current.isEmpty) Set()
+    if (current.isEmpty) Map()
     else {
-      val output = Set.newBuilder[String]
+      val output = Map.newBuilder[String, T]
       def recurse(current: Node, path: List[Char]): Unit = {
-        if (current.hasValue) output += (s + path.reverse.mkString)
+        for (v <- current.value) output += (s + path.reverse.mkString -> v)
         for ((c, n) <- current.children) recurse(n, c :: path)
       }
       recurse(current.get, Nil) // recursive walk
```
