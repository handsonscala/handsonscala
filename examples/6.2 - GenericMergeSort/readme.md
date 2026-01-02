# Example 6.2 - GenericMergeSort
Generic merge sort implemention, that can sort any `IndexedSeq[T]` with an
`Ordering`

```bash
./mill -i TestMergeSort.scala
```

## Upstream Example: [6.1 - MergeSort](https://github.com/handsonscala/handsonscala/tree/v2/examples/6.1%20-%20MergeSort):
Diff:
```diff
diff --git a/6.1 - MergeSort/MergeSort.scala b/6.2 - GenericMergeSort/MergeSort.scala
index 4dd9d5a..822ddd9 100644
--- a/6.1 - MergeSort/MergeSort.scala	
+++ b/6.2 - GenericMergeSort/MergeSort.scala	
@@ -1,17 +1,16 @@
-def mergeSort(items: Array[Int]): Array[Int] =
+def mergeSort[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] =
   if items.length <= 1 then items
   else
     val (left, right) = items.splitAt(items.length / 2)
     val (sortedLeft, sortedRight) = (mergeSort(left), mergeSort(right))
     var (leftIdx, rightIdx) = (0, 0)
-    val output = Array.newBuilder[Int]
+    val output = IndexedSeq.newBuilder[T]
     
     while leftIdx < sortedLeft.length || rightIdx < sortedRight.length do
       val takeLeft = (leftIdx < sortedLeft.length, rightIdx < sortedRight.length) match
         case (true, false) => true
         case (false, true) => false
-        case (true, true) => sortedLeft(leftIdx) < sortedRight(rightIdx)
-        case (false, false) => throw Exception("unreachable")
+        case (true, true) => Ordering[T].lt(sortedLeft(leftIdx), sortedRight(rightIdx))
       
       if takeLeft then
         output += sortedLeft(leftIdx)
diff --git a/6.1 - MergeSort/TestMergeSort.scala b/6.2 - GenericMergeSort/TestMergeSort.scala
index 4e68959..91ec3f0 100644
--- a/6.1 - MergeSort/TestMergeSort.scala	
+++ b/6.2 - GenericMergeSort/TestMergeSort.scala	
@@ -1,6 +1,9 @@
 //| moduleDeps: [MergeSort.scala]
 
 def main() =
-  val input = Array(8, 3, 5, 4, 6, 1, 0, 2, 7, 9)
+  val input = Vector("banana", "mandarin", "avocado", "apple", "mango", "cherry", "mangosteen")
 
-  assert(pprint.log(mergeSort(input)).sameElements(Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)))
+  assert(
+    pprint.log(mergeSort(input)) ==
+    Vector("apple", "avocado", "banana", "cherry", "mandarin", "mango", "mangosteen")
+  )
```
## Downstream Examples

- [13.7 - ParallelMergeSort](https://github.com/handsonscala/handsonscala/tree/v2/examples/13.7%20-%20ParallelMergeSort)