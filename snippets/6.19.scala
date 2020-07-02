+ def stringsMatchingPrefix(s: String): Set[String] = {
+   var current = Option(root)
+   for (c <- s if current.nonEmpty) current = current.get.children.get(c) // initial walk
+   if (current.isEmpty) Set()
+   else {
+     val output = Set.newBuilder[String]
+     def recurse(current: Node, path: List[Char]): Unit = {
+       if (current.hasValue) output += (s + path.reverse.mkString)
+       for ((c, n) <- current.children) recurse(n, c :: path)
+     }
+     recurse(current.get, Nil) // recursive walk
+     output.result()
+   }
+ }