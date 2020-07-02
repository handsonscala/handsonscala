import $file.Search, Search._
assert(
  pprint.log(
    shortestPath(
      start = "a",
      dest = "d",
      graph = Map(
        "a" -> Seq("b", "c"),
        "b" -> Seq("c", "d"),
        "c" -> Seq("d"),
        "d" -> Seq()
      )
    )
  ) == List("a", "c", "d")
)

assert(
  pprint.log(
    shortestPath(
      start = "a",
      dest = "c",
      graph = Map(
        "a" -> Seq("b", "c"),
        "b" -> Seq("c", "d"),
        "c" -> Seq("d"),
        "d" -> Seq()
      )
    )
  ) == List("a", "c")
)
