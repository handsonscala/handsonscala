// Numbers

assert(1 + 2 * 3 == 7)

assert((1 + 2) * 3 == 9)

assert(2147483647 + 1 == -2147483648)

assert(2147483647L + 1L == 2147483648L)

assert(java.lang.Integer.toBinaryString(123) == "1111011")

assert(java.lang.Integer.numberOfTrailingZeros(24) == 3)

// Strings

assert("hello world".substring(0, 5) == "hello")

assert("hello world".substring(5, 10) == " worl")

assert("hello" + 1 + " " + "world" + 2 == "hello1 world2")

val x = 1;
val y = 2

assert(s"Hello $x World $y" == "Hello 1 World 2")

assert(s"Hello ${x + y} World ${x - y}" == "Hello 3 World -1")

val x2 = 1

assert(x2 + 2 == 3)

var s: String = "Hello"

s = "World"

// Tuples

val t = (1, true, "hello")

assert(t._1 == 1)

assert(t._3 == "hello")

val t2: (Int, Boolean, String) = (1, true, "hello")

val (a, b, c) = t

assert(a == 1)
assert(b == true)
assert(c == "hello")

val t3 = (1, true, "hello", 'c', 0.2, 0.5f, 12345678912345L)

// Arrays

val a1 = Array[Int](1, 2, 3, 4)

assert(a1(0) == 1) // first entry, array indices start from 0

assert(a1(3) == 4) // last entry

val a2 = Array[String]("one", "two", "three", "four")

assert(a2(1) == "two") // second entry

val a3 = Array(1, 2, 3, 4)

val a4 = Array(
  "one", "two",
  "three", "four"
)

val a5 = new Array[Int](4)

a5(0) = 1

a5(2) = 100

assert(a5.toSeq == Array(1, 0, 100, 0).toSeq)

val multi = Array(Array(1, 2), Array(3, 4))

assert(multi(0)(0) == 1)
assert(multi(0)(1) == 2)
assert(multi(1)(0) == 3)
assert(multi(1)(1) == 4)

// Options

def hello(title: String, firstName: String, lastNameOpt: Option[String]) = {
  lastNameOpt match {
    case Some(lastName) => s"Hello $title. $lastName"
    case None => s"Hello $firstName"
  }
}

assert(hello("Mr", "Haoyi", None) == "Hello Haoyi")

assert(hello("Mr", "Haoyi", Some("Li")) == "Hello Mr. Li")

assert(Some("Li").getOrElse("<unknown>") == "Li")
assert(None.getOrElse("<unknown>") == "<unknown>")

def hello2(name: Option[String]) = {
  for (s <- name) println(s"Hello $s")
}

hello2(None) // does nothing

hello2(Some("Haoyi"))

def nameLength(name: Option[String]) = {
  name.map(_.length).getOrElse(-1)
}

assert(nameLength(Some("Haoyi")) == 5)

assert(nameLength(None) == -1)
