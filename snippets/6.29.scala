> searchPaths(
    start = "a",
    graph = Map(
      "a" -> Seq("b", "c"),
      "b" -> Seq("c", "d"),
      "c" -> Seq("d"),
      "d" -> Seq()
    )
  )
res22: Map[String, List[String]] = Map(
  "a" -> List("a"),
  "b" -> List("b", "a"),
  "c" -> List("c", "a"),
  "d" -> List("d", "b", "a")
)
