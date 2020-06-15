  val root = new Node(false)
+ def add(s: String) = {
+   var current = root
+   for (c <- s) current = current.children.getOrElseUpdate(c, new Node(false))
+   current.hasValue = true
+ }
}