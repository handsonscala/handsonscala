# Example 13.7 - ParallelMergeSort
Merge-sort implementation parallelized using Futures

```bash
amm TestMergeSort.sc
```

## Upstream Example: [6.2 - GenericMergeSort](https://github.com/handsonscala/handsonscala/tree/v1/examples/6.2%20-%20GenericMergeSort):
Diff:
```diff
diff --git a/6.2 - GenericMergeSort/MergeSort.sc b/13.7 - ParallelMergeSort/MergeSort.sc
index 8f5656a..fdfc7b9 100644
--- a/6.2 - GenericMergeSort/MergeSort.sc	
+++ b/13.7 - ParallelMergeSort/MergeSort.sc	
@@ -1,9 +1,30 @@
-def mergeSort[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] = {
+import scala.concurrent._, ExecutionContext.Implicits.global, duration.Duration.Inf
+
+def mergeSortParallel[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] = {
+  Await.result(mergeSortParallel0(items), Inf)
+}
+
+def mergeSortParallel0[T: Ordering](items: IndexedSeq[T]): Future[IndexedSeq[T]] = {
+  if (items.length <= 16) Future.successful(mergeSortSequential(items))
+  else {
+    val (left, right) = items.splitAt(items.length / 2)
+    mergeSortParallel0(left).zip(mergeSortParallel0(right)).map{
+      case (sortedLeft, sortedRight) => merge(sortedLeft, sortedRight)
+    }
+  }
+}
+
+def mergeSortSequential[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] = {
   if (items.length <= 1) items
   else {
     val (left, right) = items.splitAt(items.length / 2)
-    val (sortedLeft, sortedRight) = (mergeSort(left), mergeSort(right))
-    var (leftIdx, rightIdx) = (0, 0)
+    merge(mergeSortSequential(left), mergeSortSequential(right))
+  }
+}
+
+def merge[T: Ordering](sortedLeft: IndexedSeq[T], sortedRight: IndexedSeq[T]) = {
+  var leftIdx = 0
+  var rightIdx = 0
   val output = IndexedSeq.newBuilder[T]
   while (leftIdx < sortedLeft.length || rightIdx < sortedRight.length) {
     val takeLeft = (leftIdx < sortedLeft.length, rightIdx < sortedRight.length) match {
@@ -20,5 +41,4 @@ def mergeSort[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] = {
     }
   }
   output.result()
-  }
 }
diff --git a/6.2 - GenericMergeSort/TestMergeSort.sc b/13.7 - ParallelMergeSort/TestMergeSort.sc
index 0f68ad4..4d03717 100644
--- a/6.2 - GenericMergeSort/TestMergeSort.sc	
+++ b/13.7 - ParallelMergeSort/TestMergeSort.sc	
@@ -1,7 +1,26 @@
 import $file.MergeSort, MergeSort._
-val input = Vector("banana", "mandarin", "avocado", "apple", "mango", "cherry", "mangosteen")
 
+val input = Vector("banana", "mandarin", "avocado", "apple", "mango", "cherry", "mangosteen")
+pprint.log(input)
 assert(
-  pprint.log(mergeSort(input)) ==
+  pprint.log(mergeSortParallel(input)) ==
     Vector("apple", "avocado", "banana", "cherry", "mandarin", "mango", "mangosteen")
 )
+
+val random = new scala.util.Random(1337)
+val shuffledWords = Array.fill(1 * 1000 * 1000)(random.nextInt())
+
+println("Warming up...")
+mergeSortSequential(shuffledWords)
+mergeSortParallel(shuffledWords)
+mergeSortSequential(shuffledWords)
+mergeSortParallel(shuffledWords)
+
+println("Benchmarking Sequential vs Parallel Merge Sort...")
+val (sequentialResult, sequentialTime) = time{ mergeSortSequential(shuffledWords) }
+val (parallelResult, parallelTime) = time{ mergeSortParallel(shuffledWords) }
+
+pprint.log(parallelTime)
+pprint.log(sequentialTime)
+pprint.log(parallelResult == sequentialResult)
+assert(parallelTime < sequentialTime / 2)
```

The cutoff between sequential and parallel sorting is arbitrary; benchmarks show
the optimal cutoff to be somewhere around 2-32 items:

| cutover | parallel     | sequential   | speedup |
|--------:|-------------:|-------------:|--------:|
| 1       |       1995ms |       3503ms | 1.76    |
| 2       |       1383ms |       3480ms | 2.52    |
| 4       |       1491ms |       3607ms | 2.42    |
| 8       |       1695ms |       3660ms | 2.16    |
| 16      |       1332ms |       3357ms | 2.52    |
| 32      |       1391ms |       3253ms | 2.34    |
| 64      |       1645ms |       3312ms | 2.01    |
| 128     |       1645ms |       3312ms | 2.10    |
| 256     |       2000ms |       3647ms | 2.10    |
