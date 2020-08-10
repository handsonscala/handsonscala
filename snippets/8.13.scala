@ val input = """{"weasel": ["i", "am"], "baboon": ["i", "r"]}"""

@ val parsed = upickle.default.read[Map[String, Seq[String]]](input)
parsed: Map[String, Seq[String]] = Map(
  "weasel" -> List("i", "am"),
  "baboon" -> List("i", "r")
)

@ upickle.default.write(parsed)
res28: String = "{\"weasel\":[\"i\",\"am\"],\"baboon\":[\"i\",\"r\"]}"
