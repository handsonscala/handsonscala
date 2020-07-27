@ val author = upickle.default.read[Author](data(0)("author")) // read from JSON structure
author: Author = Author("Ammonite-Bot", 20607116, false)

@ author.login
res33: String = "Ammonite-Bot"

@ val author2 = upickle.default.read[Author](  // read directly from a String
    """{"login": "lihaoyi", "id": 313373, "site_admin": true}"""
  )
author2: Author = Author("lihaoyi", 313373, true)

@ upickle.default.write(author2)
res35: String = "{\"login\":\"lihaoyi\",\"id\":313373,\"site_admin\":true}"
