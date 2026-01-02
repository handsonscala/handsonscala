+ def prefixesMatchingString0(s: String): Set[Int] =
+   var current = Option(root)
+   val output = Set.newBuilder[Int]
+
+   for (c, i) <- s.zipWithIndex if current.nonEmpty do
+     if current.get.hasValue then output += i
+     current = current.get.children.get(c)
+
+   if current.exists(_.hasValue) then output += s.length
+   output.result()