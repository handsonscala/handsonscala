//| moduleDeps: [Search.scala]
def shortestPath[T](start: T, dest: T, graph: Map[T, Seq[T]]): Seq[T] =
  val shortestReversedPaths = searchPaths(start, graph)
  shortestReversedPaths(dest).reverse