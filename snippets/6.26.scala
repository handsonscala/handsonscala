> search(
    start = "a",
    graph = Map(
      "a" -> Seq("b", "c"),
      "b" -> Seq("c", "d"),
      "c" -> Seq("d"),
      "d" -> Seq()
    )
  )
res20: Set[String] = Set(
  "a", "b", "c", "d"
)
