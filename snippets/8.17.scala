@ upickle.default.read[Map[String, Author]]("""{
    "haoyi": {"login": "lihaoyi", "id": 1337, "site_admin": true},
    "bot": {"login": "ammonite-bot", "id": 31337, "site_admin": false}
  }""")
res36: Map[String, Author] = Map(
  "haoyi" -> Author("lihaoyi", 1337, true),
  "bot" -> Author("ammonite-bot", 31337, false)
)
