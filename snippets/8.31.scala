> val msg = upack.Obj(
    upack.Str("login") -> upack.Str("haoyi"),
    upack.Str("id") -> upack.Int32(31337),
    upack.Str("site_admin") -> upack.True
  )

> val blob3 = upack.write(msg)
blob3: Array[Byte] = Array(-125, -91, 108, 111, ...)

> val deserialized = upickle.readBinary[Author](blob3)
deserialized: Author = Author(
  login = "haoyi",
  id = 31337,
  site_admin = true
)
