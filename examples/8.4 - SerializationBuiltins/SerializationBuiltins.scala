def main() =
  val numbers = upickle.read[Seq[Int]]("[1, 2, 3, 4]")

  assert(upickle.write(numbers) == "[1,2,3,4]")

  val tuples = upickle.read[Seq[(Int, Boolean)]]("[[1, true], [2, false]]")

  assert(upickle.write(tuples) == "[[1,true],[2,false]]")

  val input = """{"weasel": ["i", "am"], "baboon": ["i", "r"]}"""

  val parsed = upickle.read[Map[String, Seq[String]]](input)

  assert(
    upickle.write(parsed) ==
    "{\"weasel\":[\"i\",\"am\"],\"baboon\":[\"i\",\"r\"]}"
  )
