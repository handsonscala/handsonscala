@ val input = """local greeting = "Hello "; nope + nope"""

@ evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty)
java.util.NoSuchElementException: key not found: nope
  scala.collection.immutable.Map$Map1.apply(Map.scala:242)
  ammonite.$sess.cmd93$.evaluate(cmd93.sc:10)
  ammonite.$sess.cmd93$.evaluate(cmd93.sc:5)
  ammonite.$sess.cmd95$.<clinit>(cmd95.sc:3)
