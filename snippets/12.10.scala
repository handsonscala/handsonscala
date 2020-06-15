@ def fetchPaginated(url: String, params: (String, String)*) = {
    var done = false
    var page = 1
    val responses = collection.mutable.Buffer.empty[ujson.Value]
    while (!done) {
      println("page " + page + "...")
      val resp = requests.get(
        url,
        params = Map("page" -> page.toString) ++ params,
        headers = Map("Authorization" -> s"token $token")
      )
      val parsed = ujson.read(resp.text()).arr
      if (parsed.length == 0) done = true
      else responses.appendAll(parsed)
      page += 1
    }
    responses
  }

@ val issues = fetchPaginated(
    "https://api.github.com/repos/lihaoyi/upickle/issues",
    "state" -> "all"
  )
page 1...
page 2...
page 3...
