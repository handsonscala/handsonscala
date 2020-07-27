@ parseFromString[Seq[Boolean]]("[true,false,true]") // 1 layer of nesting
res1: Seq[Boolean] = List(true, false, true)

@ parseFromString[Seq[(Seq[Int], Seq[Boolean])]]( // 3 layers of nesting
    "[[[1],[true]],[[2,3],[false,true]],[[4,5,6],[false,true,false]]]"
  )
res2: Seq[(Seq[Int], Seq[Boolean])] = List(
  (List(1), List(true)),
  (List(2, 3), List(false, true)),
  (List(4, 5, 6), List(false, true, false))
)

@ parseFromString[Seq[(Seq[Int], Seq[(Boolean, Double)])]]( // 4 layers of nesting
    "[[[1],[[true,0.5]]],[[2,3],[[false,1.5],[true,2.5]]]]"
  )
res3: Seq[(Seq[Int], Seq[(Boolean, Double)])] = List(
  (List(1), List((true, 0.5))),
  (List(2, 3), List((false, 1.5), (true, 2.5)))
)
