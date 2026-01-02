> fastparse.parse(
    "(hello  world)   ((goodbye seattle) world)",
    parser(using _)
  )
res36: fastparse.Parsed[Phrase] = Success(
  value = Pair(
    lhs = Pair(lhs = Word("hello"), rhs = Word("world")),
    rhs = Pair(
      lhs = Pair(lhs = Word("goodbye"), rhs = Word("seattle")),
      rhs = Word("world")
    )
  ),
  index = 42
)
