import $file.MergeSort, MergeSort._
val input = Vector("banana", "mandarin", "avocado", "apple", "mango", "cherry", "mangosteen")

assert(
  pprint.log(mergeSort(input)) ==
  Vector("apple", "avocado", "banana", "cherry", "mandarin", "mango", "mangosteen")
)
