> import scala.collection.mutable

> val myArrayDeque = mutable.ArrayDeque(1, 2, 3, 4, 5)

> myArrayDeque.removeHead()
res49: Int = 1

> myArrayDeque.append(6)
res50: mutable.ArrayDeque[Int] = ArrayDeque(2, 3, 4, 5, 6)

> myArrayDeque.removeHead()
res51: Int = 2

> println(myArrayDeque)
ArrayDeque(3, 4, 5, 6)
