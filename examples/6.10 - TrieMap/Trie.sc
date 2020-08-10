class Trie[T]() {
  class Node(var value: Option[T],
             val children: collection.mutable.Map[Char, Node] = collection.mutable.Map())
  val root = new Node(None)
  def add(s: String, v: T) = {
    var current = root
    for (c <- s) current = current.children.getOrElseUpdate(c, new Node(None))
    current.value = Some(v)
  }
  def contains(s: String): Boolean = get(s).isDefined
  def get(s: String): Option[T] = {
    var current = Option(root)
    for (c <- s if current.nonEmpty) current = current.get.children.get(c)
    current.flatMap(_.value)
  }
  def prefixesMatchingString0(s: String): Map[Int, T] = {
    var current = Option(root)
    val output = Map.newBuilder[Int, T]
    for ((c, i) <- s.zipWithIndex if current.nonEmpty) {
      for (v <- current.get.value) output += (i -> v)
      current = current.get.children.get(c)
    }
    for (c <- current; v <- c.value) output += (s.length -> v)
    output.result()
  }
  def prefixesMatchingString(s: String): Map[String, T] = {
    prefixesMatchingString0(s).map { case (k, v) => (s.substring(0, k), v)}
  }
  def stringsMatchingPrefix(s: String): Map[String, T] = {
    var current = Option(root)
    for (c <- s if current.nonEmpty) current = current.get.children.get(c) // initial walk
    if (current.isEmpty) Map()
    else {
      val output = Map.newBuilder[String, T]
      def recurse(current: Node, path: List[Char]): Unit = {
        for (v <- current.value) output += (s + path.reverse.mkString -> v)
        for ((c, n) <- current.children) recurse(n, c :: path)
      }
      recurse(current.get, Nil) // recursive walk
      output.result()
    }
  }
}
