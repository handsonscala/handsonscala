> def firstAndLastElements[T](arr: Array[T]): (T, T) = (arr(0), arr(arr.length - 1))

> firstAndLastElements(Array(1, 2, 3, 4, 5))
res1: (Int, Int) = (1, 5)

> firstAndLastElements(Array("i", "am", "cow"))
res2: (String, String) = ("i", "cow")
