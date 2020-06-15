val myArrayDeque = collection.mutable.ArrayDeque(1, 2, 3, 4, 5)

assert(myArrayDeque.removeHead() == 1)

myArrayDeque.append(6)

assert(myArrayDeque.removeHead() == 2)

assert(myArrayDeque == collection.mutable.ArrayDeque(3, 4, 5, 6))

assert(myArrayDeque.to(Vector) == Vector(3, 4, 5, 6))
