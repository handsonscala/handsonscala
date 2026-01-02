> val input = """local greeting = "Hello "; nope + nope"""

> evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty)
java.util.NoSuchElementException: key not found: nope
  at scala.collection.immutable.Map$Map1.apply(Map.scala:267)
  at rs$line$31$.evaluate(rs$line$31:11)
  at rs$line$31$.evaluate(rs$line$31:5)
  ... 30 elided
