val jsonString = os.read(os.pwd / "ammonite-releases.json")

val data = ujson.read(jsonString)

pprint.log(data(0), height = 20)

assert(
  pprint.log(data(0)("url")) ==
  ujson.Str("https://api.github.com/repos/lihaoyi/Ammonite/releases/17991367")
)

assert(pprint.log(data(0)("author")("id")) == ujson.Num(20607116))

pprint.log(data(0).obj, height = 20)

pprint.log(data(0).obj.keys, height = 20)

assert(pprint.log(data(0).obj.size) == 18)

assert(pprint.log(data(0)("url").str) == "https://api.github.com/repos/lihaoyi/Ammonite/releases/17991367")

assert(pprint.log(data(0)("author")("id").num) == 20607116)

assert(pprint.log(data(0)("author")("id").num.toInt) == 20607116)

data(0)("hello") = "goodbye"

data(0)("tags") = ujson.Arr("awesome", "yay", "wonderful")

data(0).obj.remove("url")

assert(data(0)("hello").str == "goodbye")

assert(data(0)("tags")  == ujson.Arr("awesome", "yay", "wonderful"))

assert(!data(0).obj.contains("url"))
