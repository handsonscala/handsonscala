> val input = """{"weasel": ["i", "am"], "baboon": ["i", "r"]}"""

> val parsed = upickle.read[Map[String, Seq[String]]](input)
parsed: Map[String, Seq[String]] = Map(
  "weasel" -> List("i", "am"),
  "baboon" -> List("i", "r")
)

> upickle.write(parsed)
res12: String = "{\"weasel\":[\"i\",\"am\"],\"baboon\":[\"i\",\"r\"]}"
