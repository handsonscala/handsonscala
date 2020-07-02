-def mergeSort(items: Array[Int]): Array[Int] = {
+def mergeSort[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] = {
