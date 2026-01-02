  val root = Node(false)
+
+ def add(s: String) =
+   var current = root
+   for c <- s do current = current.children.getOrElseUpdate(c, Node(false))
+
+   current.hasValue = true