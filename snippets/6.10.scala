class Trie():
  import collection.mutable
  class Node(var hasValue: Boolean, val children: mutable.Map[Char, Node] = mutable.Map())

  val root = Node(false)