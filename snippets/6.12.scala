+ def contains(s: String): Boolean =
+   var current = Option(root)
+   for c <- s if current.nonEmpty do current = current.get.children.get(c)
+
+   current.exists(_.hasValue)