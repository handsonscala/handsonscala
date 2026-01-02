class Trie[T]():
  import collection.mutable
  class Node(var value: Option[T], val children: mutable.Map[Char, Node] = mutable.Map())

  val root = Node(None)

  def add(s: String, v: T) =
    var current = root
    for c <- s do current = current.children.getOrElseUpdate(c, Node(None))
    current.value = Some(v)

  def contains(s: String): Boolean = get(s).isDefined

  def get(s: String): Option[T] =
    var current = Option(root)
    for c <- s if current.nonEmpty do current = current.get.children.get(c)
    current.flatMap(_.value)

  def prefixesMatchingString0(s: String): Map[Int, T] =
    var current = Option(root)
    val output = Map.newBuilder[Int, T]

    for (c, i) <- s.zipWithIndex if current.nonEmpty do
      for v <- current.get.value do output += (i -> v)
      current = current.get.children.get(c)

    for c <- current; v <- c.value do output += (s.length -> v)
    output.result()

  def prefixesMatchingString(s: String): Map[String, T] =
    prefixesMatchingString0(s).map { case (k, v) => (s.substring(0, k), v)}

  def stringsMatchingPrefix(s: String): Map[String, T] =
    var current = Option(root)
    for c <- s if current.nonEmpty do current = current.get.children.get(c) // initial walk

    if current.isEmpty then Map()
    else
      val output = Map.newBuilder[String, T]
      def recurse(current: Node, path: List[Char]): Unit =
        for v <- current.value do output += (s + path.reverse.mkString -> v)
        for (c, n) <- current.children do recurse(n, c :: path)

      recurse(current.get, Nil) // recursive walk
      output.result()
