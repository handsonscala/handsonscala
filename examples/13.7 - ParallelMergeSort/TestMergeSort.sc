import $file.MergeSort, MergeSort._

val input = Vector("banana", "mandarin", "avocado", "apple", "mango", "cherry", "mangosteen")
pprint.log(input)
assert(
  pprint.log(mergeSortParallel(input)) ==
    Vector("apple", "avocado", "banana", "cherry", "mandarin", "mango", "mangosteen")
)

val random = new scala.util.Random(1337)
val shuffledWords = Array.fill(1 * 1000 * 1000)(random.nextInt())

println("Warming up...")
mergeSortSequential(shuffledWords)
mergeSortParallel(shuffledWords)
mergeSortSequential(shuffledWords)
mergeSortParallel(shuffledWords)

println("Benchmarking Sequential vs Parallel Merge Sort...")
val (sequentialResult, sequentialTime) = time{ mergeSortSequential(shuffledWords) }
val (parallelResult, parallelTime) = time{ mergeSortParallel(shuffledWords) }

pprint.log(parallelTime)
pprint.log(sequentialTime)
pprint.log(parallelResult == sequentialResult)
assert(parallelTime < sequentialTime / 2)
