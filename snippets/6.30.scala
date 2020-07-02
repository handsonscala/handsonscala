@ shortestPath(
    start = "a",
    dest = "d",
    graph = Map(
      "a" -> Seq("b", "c"),
      "b" -> Seq("c", "d"),
      "c" -> Seq("d"),
      "d" -> Seq()
    )
  )
res1: Seq[String] = List("a", "b", "d")
