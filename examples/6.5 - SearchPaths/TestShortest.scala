//| moduleDeps: [Shortest.scala]

def main() =
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
    ) == List("a", "b", "d")
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
