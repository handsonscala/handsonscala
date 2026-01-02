> search(
         start = "c",
         graph = Map(
           "a" -> Seq("b", "c"),
           "b" -> Seq("a"),
           "c" -> Seq("b")
         )
       )
res19: Set[String] = Set(
  "a", "b", "c"
)
