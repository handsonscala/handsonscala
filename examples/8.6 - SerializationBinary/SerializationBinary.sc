val jsonString = os.read(os.pwd / "ammonite-releases.json")

val data = ujson.read(jsonString)

case class Author(login: String, id: Int, site_admin: Boolean)

implicit val authorRw = upickle.default.macroRW[Author]

val blob = upickle.default.writeBinary(Author("haoyi", 31337, true))

pprint.log(blob, height = 20)

assert(pprint.log(upickle.default.readBinary[Author](blob)) == Author("haoyi", 31337, true))

val mapListAuthors = Map(
  1 -> Nil,
  2 -> List(Author("haoyi", 1337, true), Author("lihaoyi", 31337, true))
)

val blob2 = upickle.default.writeBinary(mapListAuthors)

assert(
  pprint.log(upickle.default.readBinary[Map[Int, List[Author]]](blob2)) ==
  Map(1 -> List(), 2 -> List(Author("haoyi", 1337, true), Author("lihaoyi", 31337, true)))
)

assert(
  pprint.log(upack.read(blob), height = 20) ==
  upack.Obj(
    upack.Str("login") -> upack.Str("haoyi"),
    upack.Str("id") -> upack.Int32(31337),
    upack.Str("site_admin") -> upack.True
  )
)

pprint.log(upack.read(blob2), height = 20)

val msg = upack.Obj(
  upack.Str("login") -> upack.Str("haoyi"),
  upack.Str("id") -> upack.Int32(31337),
  upack.Str("site_admin") -> upack.True
)

val blob3 = upack.write(msg)
pprint.log(blob3, height = 20)

val deserialized = upickle.default.readBinary[Author](blob3)

assert(pprint.log(deserialized) == Author("haoyi", 31337, true))
