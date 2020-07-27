def searchPaths[T](start: T, graph: Map[T, Seq[T]]): Map[T, List[T]] = {
  val seen = collection.mutable.Map(start -> List(start))
  val queue = collection.mutable.ArrayDeque(start -> List(start))
  while (queue.nonEmpty) {
    val (current, path) = queue.removeHead()
    for (next <- graph(current) if !seen.contains(next)) {
      val newPath = next :: path
      seen(next) = newPath
      queue.append((next, newPath))
    }
  }
  seen.toMap
}

def shortestPath[T](start: T, dest: T, graph: Map[T, Seq[T]]): Seq[T] = {
  val shortestReversedPaths = searchPaths(start, graph)
  shortestReversedPaths(dest).reverse
}