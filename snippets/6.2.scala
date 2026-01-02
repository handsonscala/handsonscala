def mergeSort(items: Array[Int]): Array[Int] =
  if items.length <= 1 then items
  else
    val (left, right) = items.splitAt(items.length / 2)
    val (sortedLeft, sortedRight) = (mergeSort(left), mergeSort(right))