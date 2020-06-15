@ search(
    start = "c",
    graph = Map(
      "a" -> Seq("b", "c"),
      "b" -> Seq("a"),
      "c" -> Seq("b")
    )
  )
res0: Set[String] = Set("a", "b", "c")
