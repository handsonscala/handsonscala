@ def iterateOverSomething[T](items: Seq[T]) = {
    for (i <- items) println(i)
  }

@ iterateOverSomething(Vector(1, 2, 3))
1
2
3

@ iterateOverSomething(List(("one", 1), ("two", 2), ("three", 3)))
(one,1)
(two,2)
(three,3)
