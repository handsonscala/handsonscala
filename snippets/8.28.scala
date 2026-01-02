> val data = Map(
    1 -> Nil,
    2 -> List(Author("haoyi", 1337, true), Author("lihaoyi", 31337, true))
  )

> val blob2 = upickle.writeBinary(data)
blob2: Array[Byte] = Array(-126, 1, -112, 2, -110, ...)

> upickle.readBinary[Map[Int, List[Author]]](blob2)
res22: Map[Int, List[Author]] = Map(
  1 -> List(),
  2 -> List(
    Author(login = "haoyi", id = 1337, site_admin = true),
    Author(login = "lihaoyi", id = 31337, site_admin = true)
  )
)
