+ def stringsMatchingPrefix(s: String): Set[String] =
+   var current = Option(root)
+   for c <- s if current.nonEmpty do current = current.get.children.get(c) // initial walk
+
+   if current.isEmpty then Set()
+   else
+     val output = Set.newBuilder[String]
+     def recurse(current: Node, path: List[Char]): Unit =
+       if current.hasValue then output += (s + path.reverse.mkString)
+       for (c, n) <- current.children do recurse(n, c :: path)
+
+     recurse(current.get, Nil) // recursive walk
+     output.result()