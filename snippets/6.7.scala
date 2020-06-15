-case (true, true) => sortedLeft(leftIdx) < sortedRight(rightIdx)
+case (true, true) => Ordering[T].lt(sortedLeft(leftIdx), sortedRight(rightIdx))
