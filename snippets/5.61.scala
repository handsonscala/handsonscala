> parseFromString[Seq[(Int, Boolean)]]("1=true,2=false,3=true")
res23: Seq[(Int, Boolean)] = ArraySeq((1, true), (2, false), (3, true))

> parseFromString[(Seq[Int], Seq[Boolean])]("1,2,3=true,false,true")
res24: (Seq[Int], Seq[Boolean]) = (ArraySeq(1, 2, 3), ArraySeq(true, false, true))
