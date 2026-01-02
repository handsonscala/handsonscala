> def fetchPaginated(url: String, params: (String, String)*) =
    var done = false
    var page = 1
    val responses = collection.mutable.Buffer.empty[ujson.Value]
    
    while !done do
      println("page " + page + "...")

      val resp = requests.get(
        url,
        params = Map("page" -> page.toString) ++ params,
        headers = Map("Authorization" -> s"token $token")
      )

      val parsed = ujson.read(resp).arr

      if parsed.length == 0 then done = true
      else responses.appendAll(parsed)

      page += 1

    responses

> val issues = fetchPaginated(
    "https://api.github.com/repos/com-lihaoyi/upickle/issues",
    "state" -> "all"
  )
page 1...
page 2...
page 3...
