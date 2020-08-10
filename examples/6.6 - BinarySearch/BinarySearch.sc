def binarySearch[T: Ordering](sorted: IndexedSeq[T], target: T): Boolean = {
  def binarySearch0(start: Int, end: Int): Boolean = {
    // If the sequence you are trying to search has no items, the item cannot be found
    if (start == end) false
    else {
      val middle = (start + end) / 2
      // Otherwise, take the item at the middle of the sequence
      val middleItem = sorted(middle)
      // and compare it to the item you are looking for
      val comparison = Ordering[T].compare(target, middleItem)
      // If the item is what you are looking for, you have found it
      if (comparison == 0) true
      // Otherwise, if the item is greater than the one you are looking for,
      // binary search the left half of the sequence
      else if (comparison < 0) binarySearch0(start, middle)
      // If it is less than the item you are looking for, binary search the right half
      else binarySearch0(middle + 1, end)
    }
  }
  binarySearch0(0, sorted.length)
}
