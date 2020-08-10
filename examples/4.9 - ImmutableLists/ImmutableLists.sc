val myList = List(1, 2, 3, 4, 5)

assert(myList.head == 1)

val myTail = myList.tail

assert(myTail == List(2, 3, 4, 5))

val myOtherList = 0 :: myList

assert(myOtherList == List(0, 1, 2, 3, 4, 5))

val myThirdList = -1 :: myList

assert(myThirdList == List(-1, 1, 2, 3, 4, 5))
