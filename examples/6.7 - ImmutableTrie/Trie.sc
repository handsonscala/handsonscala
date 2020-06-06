
class ImmutableTrie(inputs: Seq[String]) {
  class Node(index: Int, inputs: Seq[String]) {
    val hasValue = inputs.exists(_.length == index)
    val children = {
      val filteredInputs = inputs.filter(_.length > index)
      for((childChar, childInputs) <- filteredInputs.groupBy(_.charAt(index)))
      yield (childChar, new Node(index + 1, childInputs))
    }
  }

  val root = new Node(0, inputs)

  def contains(s: String): Boolean = {
    var current = Option(root)
    for (c <- s if current.nonEmpty) current = current.get.children.get(c)
    current.exists(_.hasValue)
  }
  def prefixesMatchingString0(s: String): Set[Int] = {
    var current = Option(root)
    val output = Set.newBuilder[Int]
    for ((c, i) <- s.zipWithIndex if current.nonEmpty) {
      if (current.get.hasValue) output += i
      current = current.get.children.get(c)
    }
    if (current.exists(_.hasValue)) output += s.length
    output.result()
  }
  def prefixesMatchingString(s: String): Set[String] = {
    prefixesMatchingString0(s).map(s.substring(0, _))
  }
  def stringsMatchingPrefix(s: String): Set[String] = {
    var current = Option(root)
    for (c <- s if current.nonEmpty) current = current.get.children.get(c) // initial walk
    if (current.isEmpty) Set()
    else {
      val output = Set.newBuilder[String]
      def recurse(current: Node, path: List[Char]): Unit = {
        if (current.hasValue) output += (s + path.reverse.mkString)
        for ((c, n) <- current.children) recurse(n, c :: path)
      }
      recurse(current.get, Nil) // recursive walk
      output.result()
    }
  }
}
