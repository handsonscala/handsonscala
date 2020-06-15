@ parseFromString[Seq[(Int, Boolean)]]("1=true,2=false,3=true,4=false")
res104: Seq[(Int, Boolean)] = ArraySeq((1, true), (2, false), (3, true), (4, false))

@ parseFromString[(Seq[Int], Seq[Boolean])]("1,2,3,4,5=true,false,true")
res105: (Seq[Int], Seq[Boolean]) = (ArraySeq(1, 2, 3, 4, 5), ArraySeq(true, false, true))
