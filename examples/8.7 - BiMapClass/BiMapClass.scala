def main() =
  class Foo(val i: Int, val s: String)

  given fooRw: upickle.ReadWriter[Foo] =
    upickle.readwriter[ujson.Value].bimap[Foo](
      foo => ujson.Obj("i" -> foo.i, "s" -> foo.s),
      value => Foo(value("i").num.toInt, value("s").str)
    )

  val foo = Foo(1337, "mooo")

  val serialized = upickle.write(foo)

  assert(serialized == """{"i":1337,"s":"mooo"}""")

  val deserialized = upickle.read[Foo](serialized)

  assert(foo.i == deserialized.i)
  assert(foo.s == deserialized.s)
