class Trie():
  import collection.mutable
  class Node(var hasValue: Boolean, val children: mutable.Map[Char, Node] = mutable.Map())

  val root = Node(false)

  def add(s: String) =
    var current = root
    for c <- s do current = current.children.getOrElseUpdate(c, Node(false))
    current.hasValue = true

  def contains(s: String): Boolean =
    var current = Option(root)
    for c <- s if current.nonEmpty do current = current.get.children.get(c)
    current.exists(_.hasValue)

  def prefixesMatchingString0(s: String): Set[Int] =
    var current = Option(root)
    val output = Set.newBuilder[Int]

    for (c, i) <- s.zipWithIndex if current.nonEmpty do
      if current.get.hasValue then output += i
      current = current.get.children.get(c)

    if current.exists(_.hasValue) then output += s.length
    output.result()

  def prefixesMatchingString(s: String): Set[String] =
    prefixesMatchingString0(s).map(s.substring(0, _))

  def stringsMatchingPrefix(s: String): Set[String] =
    var current = Option(root)
    for c <- s if current.nonEmpty do current = current.get.children.get(c) // initial walk

    if current.isEmpty then Set()
    else
      val output = Set.newBuilder[String]
      def recurse(current: Node, path: List[Char]): Unit =
        if current.hasValue then output += (s + path.reverse.mkString)
        for (c, n) <- current.children do recurse(n, c :: path)
      
      recurse(current.get, Nil) // recursive walk
      output.result()
