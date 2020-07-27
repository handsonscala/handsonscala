import $file.Search, Search._

assert(
  pprint.log(
    search(
      start = "c",
      graph = Map(
        "a" -> Seq("b", "c"),
        "b" -> Seq("a"),
        "c" -> Seq("b")
      )
    )
  ) ==
    Set("a", "b", "c")
)

assert(
  pprint.log(
    search(
      start = "a",
      graph = Map(
        "a" -> Seq("b", "c"),
        "b" -> Seq("c", "d"),
        "c" -> Seq("d"),
        "d" -> Seq()
      )
    )
  ) ==
    Set("a", "b", "c", "d")
)

assert(
  pprint.log(
    search(
      start = "c",
      graph = Map(
        "a" -> Seq("b", "c"),
        "b" -> Seq("c", "d"),
        "c" -> Seq("d"),
        "d" -> Seq()
      )
    )
  ) ==
    Set("c", "d")
)
