def depthSearchPaths[T](start: T, graph: Map[T, Seq[T]]): Map[T, List[T]] = {
  val seen = collection.mutable.Map(start -> List(start))
  val pathLengths = collection.mutable.Map(start -> 0)
  val queue = collection.mutable.ArrayDeque((start, List(start), 0))
  while (queue.nonEmpty) {
    val (current, path, pathLength) = queue.removeLast()
    for {
      next <- graph(current)
      if !seen.contains(next) && !pathLengths.get(next).exists(_ <= pathLength + 1)
    } {
      val newPath = next :: path
      seen(next) = newPath
      pathLengths(next) = pathLength + 1
      queue.append((next, newPath, pathLength + 1))
    }
  }
  seen.toMap
}

def shortestPath[T](start: T, dest: T, graph: Map[T, Seq[T]]): Seq[T] = {
  val shortestReversedPaths = depthSearchPaths(start, graph)
  shortestReversedPaths(dest).reverse
}
