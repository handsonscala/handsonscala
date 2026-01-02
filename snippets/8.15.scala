> val author = upickle.read[Author](data(0)("author")) // read uJson
author: Author = Author(login = "Ammonite-Bot", id = 20607116, site_admin = false)

> author.login
res14: String = "Ammonite-Bot"

> val author2 = upickle.read[Author](  // read directly from a String
    """{"login": "lihaoyi", "id": 313373, "site_admin": true}"""
  )
author2: Author = Author(login = "lihaoyi", id = 313373, site_admin = true)

> upickle.write(author2)
res15: String = "{\"login\":\"lihaoyi\",\"id\":313373,\"site_admin\":true}"
