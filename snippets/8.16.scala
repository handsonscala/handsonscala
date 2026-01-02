> upickle.read[Map[String, Author]]("""{
    "haoyi": {"login": "lihaoyi", "id": 1337, "site_admin": true},
    "bot": {"login": "ammonite-bot", "id": 31337, "site_admin": false}
  }""")
res16: Map[String, Author] = Map(
  "haoyi" -> Author(login = "lihaoyi", id = 1337, site_admin = true),
  "bot" -> Author(login = "ammonite-bot", id = 31337, site_admin = false)
)
