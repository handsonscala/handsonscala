@ val msg = upack.Obj(
    upack.Str("login") -> upack.Str("haoyi"),
    upack.Str("id") -> upack.Int32(31337),
    upack.Str("site_admin") -> upack.True
  )

@ val blob3 = upack.write(msg)
blob3: Array[Byte] = Array(
  -125,
  -91,
  108,
...

@ val deserialized = upickle.default.readBinary[Author](blob3)
deserialized: Author = Author("haoyi", 31337, true)
