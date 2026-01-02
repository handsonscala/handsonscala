def searchPaths[T](start: T, graph: Map[T, Seq[T]]): Map[T, List[T]] =
  val seen = collection.mutable.Map(start -> List(start))
  val queue = collection.mutable.ArrayDeque(start -> List(start))

  while queue.nonEmpty do
    val (current, path) = queue.removeHead()
    for next <- graph(current) if !seen.contains(next) do
      val newPath = next :: path
      seen(next) = newPath
      queue.append((next, newPath))

  seen.toMap
