
val numbers = upickle.default.read[Seq[Int]]("[1, 2, 3, 4]")

assert(upickle.default.write(numbers) == "[1,2,3,4]")

val tuples = upickle.default.read[Seq[(Int, Boolean)]]("[[1, true], [2, false]]")

assert(upickle.default.write(tuples) == "[[1,true],[2,false]]")

val input = """{"weasel": ["i", "am"], "baboon": ["i", "r"]}"""

val parsed = upickle.default.read[Map[String, Seq[String]]](input)

assert(
  upickle.default.write(parsed) ==
  "{\"weasel\":[\"i\",\"am\"],\"baboon\":[\"i\",\"r\"]}"
)
