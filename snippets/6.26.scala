@ search(
    start = "a",
    graph = Map(
      "a" -> Seq("b", "c"),
      "b" -> Seq("c", "d"),
      "c" -> Seq("d"),
      "d" -> Seq()
    )
  )
res1: Set[String] = Set("a", "b", "c", "d")
