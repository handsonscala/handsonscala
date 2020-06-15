@ writeToString[Seq[Boolean]](Seq(true, false, true))
res1: String = "[true,false,true]"

@ writeToString(Seq(true, false, true)) // type can be inferred
res2: String = "[true,false,true]"

@ writeToString[Seq[(Seq[Int], Seq[Boolean])]](
    Seq(
      (Seq(1), Seq(true)),
      (Seq(2, 3), Seq(false, true)),
      (Seq(4, 5, 6), Seq(false, true, false))
    )
  )
res3: String = "[[[1],[true]],[[2,3],[false,true]],[[4,5,6],[false,true,false]]]"

@ writeToString(
    Seq(
      (Seq(1), Seq((true, 0.5))),
      (Seq(2, 3), Seq((false, 1.5), (true, 2.5)))
    )
  )
res4: String = "[[[1],[[true,0.5]]],[[2,3],[[false,1.5],[true,2.5]]]]"
