@ val data = Map(
    1 -> Nil,
    2 -> List(Author("haoyi", 1337, true), Author("lihaoyi", 31337, true))
  )

@ val blob2 = upickle.default.writeBinary(data)
blob2: Array[Byte] = Array(
  -110,
  -110,
  1,
  -112,
...

@ upickle.default.readBinary[Map[Int, List[Author]]](blob2)
res57: Map[Int, List[Author]] = Map(
  1 -> List(),
  2 -> List(Author("haoyi", 1337, true), Author("lihaoyi", 31337, true))
)
