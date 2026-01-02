> def getMiddleItem[T](items: IndexedSeq[T]) = items(items.length / 2)

> getMiddleItem(Vector(1, 2, 3, 4, 5))
res65: Int = 3

> getMiddleItem(Array(2, 4, 6))
res66: Int = 4
