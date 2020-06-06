@ case class Author(login: String, id: Int, site_admin: Boolean)

@ implicit val authorRw = upickle.default.macroRW[Author]

@ val blob = upickle.default.writeBinary(Author("haoyi", 31337, true))
blob: Array[Byte] = Array(
  -125,
  -91,
  108,
  111,
...

@ upickle.default.readBinary[Author](blob)
res54: Author = Author("haoyi", 31337, true)
