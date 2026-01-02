# Example 13.7 - ParallelMergeSort
Merge-sort implementation parallelized using Futures

```bash
./mill -i TestMergeSort.scala
```

## Upstream Example: [6.2 - GenericMergeSort](https://github.com/handsonscala/handsonscala/tree/v2/examples/6.2%20-%20GenericMergeSort):
Diff:
```diff
diff --git a/6.2 - GenericMergeSort/MergeSort.scala b/13.7 - ParallelMergeSort/MergeSort.scala
index 822ddd9..048d671 100644
--- a/6.2 - GenericMergeSort/MergeSort.scala	
+++ b/13.7 - ParallelMergeSort/MergeSort.scala	
@@ -1,11 +1,17 @@
-def mergeSort[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] =
-  if items.length <= 1 then items
-  else
-    val (left, right) = items.splitAt(items.length / 2)
-    val (sortedLeft, sortedRight) = (mergeSort(left), mergeSort(right))
-    var (leftIdx, rightIdx) = (0, 0)
-    val output = IndexedSeq.newBuilder[T]
+import scala.concurrent.*, duration.Duration.Inf, java.util.concurrent.Executors
+val service = Executors.newFixedThreadPool(8)
+given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)
 
+def time[T](op: => T) =
+  val start = System.nanoTime
+  val value = op
+  val taken = System.nanoTime - start
+  (value, duration.FiniteDuration(taken, "nanos"))
+
+def merge[T: Ordering](sortedLeft: IndexedSeq[T], sortedRight: IndexedSeq[T]) =
+  var leftIdx = 0
+  var rightIdx = 0
+  val output = IndexedSeq.newBuilder[T]
   while leftIdx < sortedLeft.length || rightIdx < sortedRight.length do
     val takeLeft = (leftIdx < sortedLeft.length, rightIdx < sortedRight.length) match
       case (true, false) => true
@@ -18,5 +24,20 @@ def mergeSort[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] =
     else
       output += sortedRight(rightIdx)
       rightIdx += 1
-
   output.result()
+
+def mergeSortSequential[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] =
+  if items.length <= 1 then items
+  else
+    val (left, right) = items.splitAt(items.length / 2)
+    merge(mergeSortSequential(left), mergeSortSequential(right))
+
+def mergeSortParallel0[T: Ordering](items: IndexedSeq[T]): Future[IndexedSeq[T]] =
+  if items.length <= 16 then Future.successful(mergeSortSequential(items))
+  else
+    val (left, right) = items.splitAt(items.length / 2)
+    mergeSortParallel0(left).zip(mergeSortParallel0(right)).map:
+      case (sortedLeft, sortedRight) => merge(sortedLeft, sortedRight)
+
+def mergeSortParallel[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] =
+  Await.result(mergeSortParallel0(items), Inf)
diff --git a/6.2 - GenericMergeSort/TestMergeSort.scala b/13.7 - ParallelMergeSort/TestMergeSort.scala
index 91ec3f0..0f3ce11 100644
--- a/6.2 - GenericMergeSort/TestMergeSort.scala	
+++ b/13.7 - ParallelMergeSort/TestMergeSort.scala	
@@ -2,8 +2,30 @@
 
 def main() =
   val input = Vector("banana", "mandarin", "avocado", "apple", "mango", "cherry", "mangosteen")
+  pprint.log(input)
 
+  try
     assert(
-    pprint.log(mergeSort(input)) ==
+      pprint.log(mergeSortParallel(input)) ==
         Vector("apple", "avocado", "banana", "cherry", "mandarin", "mango", "mangosteen")
     )
+
+    val random = new scala.util.Random(1337)
+    val shuffledWords = Array.fill(1 * 1000 * 1000)(random.nextInt())
+
+    println("Warming up...")
+    mergeSortSequential(shuffledWords)
+    mergeSortParallel(shuffledWords)
+    mergeSortSequential(shuffledWords)
+    mergeSortParallel(shuffledWords)
+
+    println("Benchmarking Sequential vs Parallel Merge Sort...")
+    val (sequentialResult, sequentialTime) = time{ mergeSortSequential(shuffledWords) }
+    val (parallelResult, parallelTime) = time{ mergeSortParallel(shuffledWords) }
+
+    pprint.log(parallelTime)
+    pprint.log(sequentialTime)
+    pprint.log(parallelResult == sequentialResult)
+    assert(parallelTime < sequentialTime / 2)
+  finally
+    service.shutdown()
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
