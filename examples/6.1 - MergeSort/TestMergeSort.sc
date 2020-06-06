import $file.MergeSort, MergeSort._

val input = Array(8, 3, 5, 4, 6, 1, 0, 2, 7, 9)

assert(pprint.log(mergeSort(input)).sameElements(Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)))
