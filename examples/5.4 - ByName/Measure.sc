
def measureTime(f: => Unit) = {
  val start = System.currentTimeMillis()
  f
  val end = System.currentTimeMillis()
  println("Evaluation took " + (end - start) + " milliseconds")
}

measureTime(new Array[String](10 * 1000 * 1000).hashCode())

measureTime { // methods taking a single arg can also be called with curly brackets
  new Array[String](100 * 1000 * 1000).hashCode()
}
