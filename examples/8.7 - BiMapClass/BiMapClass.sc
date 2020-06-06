class Foo(val i: Int, val s: String)

implicit val fooRw = upickle.default.readwriter[ujson.Value].bimap[Foo](
  foo => ujson.Obj("i" -> foo.i, "s" -> foo.s),
  value => new Foo(value("i").num.toInt, value("s").str)
)

val foo = new Foo(1337, "mooo")

val serialized = upickle.default.write(foo)

assert(serialized == """{"i":1337,"s":"mooo"}""")

val deserialized = upickle.default.read[Foo](serialized)

assert(foo.i == deserialized.i)
assert(foo.s == deserialized.s)
