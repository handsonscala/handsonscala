# Example 6.2 - GenericMergeSort
Generic merge sort implemention, that can sort any `IndexedSeq[T]` with an
`Ordering`

```bash
amm TestMergeSort.sc
```

## Upstream Example: [6.1 - MergeSort](https://github.com/handsonscala/handsonscala/tree/v1/examples/6.1%20-%20MergeSort):
Diff:
```diff
diff --git a/6.1 - MergeSort/MergeSort.sc b/6.2 - GenericMergeSort/MergeSort.sc
index 19775e3..8f5656a 100644
--- a/6.1 - MergeSort/MergeSort.sc	
+++ b/6.2 - GenericMergeSort/MergeSort.sc	
@@ -1,15 +1,15 @@
-def mergeSort(items: Array[Int]): Array[Int] = {
+def mergeSort[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] = {
   if (items.length <= 1) items
   else {
     val (left, right) = items.splitAt(items.length / 2)
     val (sortedLeft, sortedRight) = (mergeSort(left), mergeSort(right))
     var (leftIdx, rightIdx) = (0, 0)
-    val output = Array.newBuilder[Int]
+    val output = IndexedSeq.newBuilder[T]
     while (leftIdx < sortedLeft.length || rightIdx < sortedRight.length) {
       val takeLeft = (leftIdx < sortedLeft.length, rightIdx < sortedRight.length) match {
         case (true, false) => true
         case (false, true) => false
-        case (true, true) => sortedLeft(leftIdx) < sortedRight(rightIdx)
+        case (true, true) => Ordering[T].lt(sortedLeft(leftIdx), sortedRight(rightIdx))
       }
       if (takeLeft) {
         output += sortedLeft(leftIdx)
diff --git a/6.1 - MergeSort/TestMergeSort.sc b/6.2 - GenericMergeSort/TestMergeSort.sc
index 49cefa5..0f68ad4 100644
--- a/6.1 - MergeSort/TestMergeSort.sc	
+++ b/6.2 - GenericMergeSort/TestMergeSort.sc	
@@ -1,5 +1,7 @@
 import $file.MergeSort, MergeSort._
+val input = Vector("banana", "mandarin", "avocado", "apple", "mango", "cherry", "mangosteen")
 
-val input = Array(8, 3, 5, 4, 6, 1, 0, 2, 7, 9)
-
-assert(pprint.log(mergeSort(input)).sameElements(Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)))
+assert(
+  pprint.log(mergeSort(input)) ==
+  Vector("apple", "avocado", "banana", "cherry", "mandarin", "mango", "mangosteen")
+)
```
## Downstream Examples

- [13.7 - ParallelMergeSort](https://github.com/handsonscala/handsonscala/tree/v1/examples/13.7%20-%20ParallelMergeSort)