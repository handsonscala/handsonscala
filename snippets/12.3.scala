val r = requests.post("http://httpbin.org/post", data = Map("key" -> "value"))

val r = requests.put("http://httpbin.org/put", data = Map("key" -> "value"))

val r = requests.delete("http://httpbin.org/delete")

val r = requests.options("http://httpbin.org/get")
