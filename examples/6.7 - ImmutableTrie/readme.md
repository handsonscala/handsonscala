# Example 6.7 - ImmutableTrie
Immutable trie implementation

```bash
amm TestTrie.sc
```

## Upstream Example: [6.3 - Trie](https://github.com/handsonscala/handsonscala/tree/v1/examples/6.3%20-%20Trie):
Diff:
```diff
diff --git a/6.3 - Trie/TestTrie.sc b/6.7 - ImmutableTrie/TestTrie.sc
index cc75158..a42c1ef 100644
--- a/6.3 - Trie/TestTrie.sc	
+++ b/6.7 - ImmutableTrie/TestTrie.sc	
@@ -1,11 +1,6 @@
 import $file.Trie, Trie._
 
-val t = new Trie()
-
-t.add("mango")
-t.add("mandarin")
-t.add("map")
-t.add("man")
+val t = new ImmutableTrie(Seq("mango", "mandarin", "map", "man"))
 
 assert(pprint.log(t.contains("mango")) == true)
 
diff --git a/6.3 - Trie/Trie.sc b/6.7 - ImmutableTrie/Trie.sc
index 9325268..ad4d52b 100644
--- a/6.3 - Trie/Trie.sc	
+++ b/6.7 - ImmutableTrie/Trie.sc	
@@ -1,12 +1,16 @@
-class Trie() {
-  class Node(var hasValue: Boolean,
-             val children: collection.mutable.Map[Char, Node] = collection.mutable.Map())
-  val root = new Node(false)
-  def add(s: String) = {
-    var current = root
-    for (c <- s) current = current.children.getOrElseUpdate(c, new Node(false))
-    current.hasValue = true
-  }
+
+class ImmutableTrie(inputs: Seq[String]) {
+  class Node(index: Int, inputs: Seq[String]) {
+    val hasValue = inputs.exists(_.length == index)
+    val children = {
+      val filteredInputs = inputs.filter(_.length > index)
+      for((childChar, childInputs) <- filteredInputs.groupBy(_.charAt(index)))
+      yield (childChar, new Node(index + 1, childInputs))
+    }
+  }
+
+  val root = new Node(0, inputs)
+
   def contains(s: String): Boolean = {
     var current = Option(root)
     for (c <- s if current.nonEmpty) current = current.get.children.get(c)
```
