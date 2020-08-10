import $file.BinarySearch, BinarySearch._

assert(pprint.log(binarySearch(Array(1, 3, 7, 9, 13), 3)) == true)

assert(pprint.log(binarySearch(Array(1, 3, 7, 9, 13), 9)) == true)

assert(pprint.log(binarySearch(Array(1, 3, 7, 9, 13), 7)) == true)

assert(pprint.log(binarySearch(Array(1, 3, 7, 9, 13), 8)) == false)

assert(pprint.log(binarySearch(Array(1, 3, 7, 9, 13), 2)) == false)

assert(pprint.log(binarySearch(Array(1, 3, 7, 9, 13), 100)) == false)

assert(pprint.log(binarySearch(Vector("i", "am", "cow", "hear", "me", "moo"), "cow")) == true)

assert(pprint.log(binarySearch(Vector("i", "am", "cow", "hear", "me", "moo"), "moo")) == true)

assert(pprint.log(binarySearch(Vector("i", "am", "cow", "hear", "me", "moo"), "horse")) == false)
