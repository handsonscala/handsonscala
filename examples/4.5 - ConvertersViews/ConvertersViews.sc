assert(Array(1, 2, 3).to(Vector) == Vector(1, 2, 3))

assert(Vector(1, 2, 3).to(Array).toSeq == Array(1, 2, 3).toSeq)

assert(Array(1, 1, 2, 2, 3, 4).to(Set) == Set(1, 2, 3, 4))

val myArray = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)

val myNewArray = myArray.map(x => x + 1).filter(x => x % 2 == 0).slice(1, 3)

assert(myNewArray.toSeq == Array(4, 6).toSeq)

val myNewArray2 = myArray.view.map(_ + 1).filter(_ % 2 == 0).slice(1, 3).to(Array)

assert(myNewArray2.toSeq == Array(4, 6).toSeq)
