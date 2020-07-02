import $file.TypeclassInference, TypeclassInference._

val args = Seq("123", "true", "7.5")
val myInt = parseFromString[Int](args(0))
val myBoolean = parseFromString[Boolean](args(1))
val myDouble = parseFromString[Double](args(2))

assert(myInt == 123)
assert(myBoolean == true)
assert(myDouble == 7.5)

assert(parseFromString[Seq[Boolean]]("true,false,true") == Seq(true, false, true))
assert(parseFromString[Seq[Int]]("1,2,3,4") == Seq(1, 2, 3, 4))

assert(parseFromString[(Int, Boolean)]("123=true") == (123, true))
assert(parseFromString[(Boolean, Double)]("true=1.5") == (true, 1.5))

assert(
  parseFromString[Seq[(Int, Boolean)]]("1=true,2=false,3=true,4=false") ==
  Seq((1, true), (2, false), (3, true), (4, false))
)

assert(
  parseFromString[(Seq[Int], Seq[Boolean])]("1,2,3,4,5=true,false,true") ==
  (Seq(1, 2, 3, 4, 5), Seq(true, false, true))
)
