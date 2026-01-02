$ ./mill --repl

> val r = requests.get("https://api.github.com/users/lihaoyi")
r: requests.Response = Response(...)

> r.statusCode
res0: Int = 200

> r.headers("content-type")
res1: Seq[String] = List("application/json; charset=utf-8")

> r.text()
res2: String = "{\"login\":\"lihaoyi\",\"id\":934140,\"node_id\":\"MDQ6VX..."
