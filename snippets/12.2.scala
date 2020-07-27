@ val r = requests.get("https://api.github.com/users/lihaoyi")

@ r.statusCode
res1: Int = 200

@ r.headers("content-type")
res2: Seq[String] = List("application/json; charset=utf-8")

@ r.text
res3: String = "{\"login\":\"lihaoyi\",\"id\":934140,\"node_id\":\"MDQ6VXNlcjkzNDE0MA...
