import $file.Serialize, Serialize._

val myInt = writeToString[Int](123)
val myBoolean = writeToString(true)
val myDouble = writeToString(7.5)

assert(myInt == "123")
assert(myBoolean == "true")
assert(myDouble == "7.5")

assert(writeToString(Seq(true, false, true)) == "[true,false,true]")
assert(writeToString(Seq(1, 2, 3, 4)) == "[1,2,3,4]")

assert(writeToString[(Int, Boolean)]((123, true)) == "[123,true]")
assert(writeToString[(Boolean, Double)]((true, 1.5)) == "[true,1.5]")

assert(
  writeToString(Seq((1, true), (2, false), (3, true), (4, false))) ==
  "[[1,true],[2,false],[3,true],[4,false]]"
)

assert(
  writeToString((Seq(1, 2, 3, 4, 5), Seq(true, false, true))) ==
  "[[1,2,3,4,5],[true,false,true]]"
)

val nested = writeToString(
  Seq(
    (Seq(1), Seq(true)),
    (Seq(2, 3), Seq(false, true)),
    (Seq(4, 5, 6), Seq(false, true, false))
  )
)
pprint.log(nested)
assert(
  nested == "[[[1],[true]],[[2,3],[false,true]],[[4,5,6],[false,true,false]]]"
)

val nested2 = writeToString(
  Seq(
    (Seq(1), Seq((true, 0.5))),
    (Seq(2, 3), Seq((false, 1.5), (true, 2.5)))
  )
)

pprint.log(nested2)

assert(
  nested2 ==
  "[[[1],[[true,0.5]]],[[2,3],[[false,1.5],[true,2.5]]]]"

)
