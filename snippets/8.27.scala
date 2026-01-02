> val blob = upickle.writeBinary(Author("haoyi", 31337, true))
blob: Array[Byte] = Array(-125, -91, 108, 111, ...)

> upickle.readBinary[Author](blob)
res21: Author = Author(login = "haoyi", id = 31337, site_admin = true)
