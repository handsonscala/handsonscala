def isValidSudoku(grid: Array[Array[Int]]): Boolean = {
  !Range(0, 9).exists{i =>
    val row = Range(0, 9).map(grid(i)(_)).filter(_ != 0)
    val col = Range(0, 9).map(grid(_)(i)).filter(_ != 0)
    val square = Range(0, 9).map(j => grid((i % 3) * 3 + j % 3)((i / 3) * 3 + j / 3)).filter(_ != 0)
    row.distinct.length != row.length ||
    col.distinct.length != col.length ||
    square.distinct.length != square.length
  }
}

assert(
  isValidSudoku(Array(
    Array(3, 0, 6,   5, 0, 8,   4, 0, 0),
    Array(5, 2, 0,   0, 0, 0,   0, 0, 0),
    Array(0, 8, 7,   0, 0, 0,   0, 3, 1),

    Array(0, 0, 3,   0, 1, 0,   0, 8, 0),
    Array(9, 0, 0,   8, 6, 3,   0, 0, 5),
    Array(0, 5, 0,   0, 9, 0,   6, 0, 0),

    Array(1, 3, 0,   0, 0, 0,   2, 5, 0),
    Array(0, 0, 0,   0, 0, 0,   0, 7, 4),
    Array(0, 0, 5,   2, 0, 6,   3, 0, 0)
  )) == true
)

assert(
  isValidSudoku(Array(
    Array(3, 0, 6,   5, 0, 8,   4, 0, 0),
    Array(5, 2, 0,   0, 0, 0,   0, 0, 0),
    Array(0, 8, 7,   0, 0, 0,   0, 3, 1),

    Array(0, 0, 3,   0, 1, 0,   0, 8, 0),
    Array(9, 0, 0,   8, 6, 3,   0, 0, 5),
    Array(0, 5, 0,   0, 9, 0,   6, 0, 0),

    Array(1, 3, 0,   0, 0, 0,   2, 5, 0),
    Array(0, 0, 0,   0, 0, 0,   0, 7, 4),
    Array(0, 0, 5,   2, 0, 6,   2, 0, 0)
  )) == false
)

assert(
  isValidSudoku(Array(
    Array(3, 1, 6,   5, 7, 8,   4, 9, 2),
    Array(5, 2, 9,   1, 3, 4,   7, 6, 8),
    Array(4, 8, 7,   6, 2, 9,   5, 3, 1),

    Array(2, 6, 3,   0, 1, 0,   0, 8, 0),
    Array(9, 7, 4,   8, 6, 3,   0, 0, 5),
    Array(8, 5, 1,   0, 9, 0,   6, 0, 0),

    Array(1, 3, 0,   0, 0, 0,   2, 5, 0),
    Array(0, 0, 0,   0, 0, 0,   0, 7, 4),
    Array(0, 0, 5,   2, 0, 6,   3, 0, 0)
  )) == true
)

assert(
  isValidSudoku(Array(
    Array(3, 1, 6,   5, 7, 8,   4, 9, 2),
    Array(5, 2, 9,   1, 3, 4,   7, 6, 8),
    Array(4, 8, 7,   6, 2, 9,   5, 3, 1),

    Array(2, 6, 3,   0, 1, 0,   0, 8, 0),
    Array(9, 7, 4,   8, 6, 3,   0, 0, 5),
    Array(8, 5, 1,   0, 9, 0,   6, 0, 0),

    Array(1, 3, 0,   0, 0, 0,   2, 5, 0),
    Array(0, 0, 0,   0, 0, 0,   0, 7, 4),
    Array(0, 0, 5,   2, 0, 6,   2, 0, 0)
  )) == false
)

assert(
  isValidSudoku(Array(
    Array(3, 1, 6,   5, 7, 8,   4, 9, 2),
    Array(5, 2, 9,   1, 3, 4,   7, 6, 8),
    Array(4, 8, 7,   6, 2, 9,   5, 3, 1),

    Array(2, 6, 3,   4, 1, 5,   9, 8, 7),
    Array(9, 7, 4,   8, 6, 3,   1, 2, 5),
    Array(8, 5, 1,   7, 9, 2,   6, 4, 3),

    Array(1, 3, 8,   9, 4, 7,   2, 5, 6),
    Array(6, 9, 2,   3, 5, 1,   8, 7, 4),
    Array(7, 4, 5,   2, 8, 6,   3, 1, 9)
  )) == true
)

assert(
  isValidSudoku(Array(
    Array(3, 1, 6,   5, 7, 8,   4, 9, 2),
    Array(5, 2, 9,   1, 3, 4,   7, 6, 8),
    Array(4, 8, 7,   6, 2, 9,   5, 3, 1),

    Array(2, 6, 3,   4, 1, 5,   9, 8, 7),
    Array(9, 7, 4,   8, 6, 3,   1, 2, 5),
    Array(8, 5, 1,   7, 9, 2,   6, 4, 3),

    Array(1, 3, 8,   9, 4, 7,   2, 5, 6),
    Array(6, 9, 2,   3, 5, 1,   8, 7, 4),
    Array(7, 4, 5,   2, 8, 6,   3, 1, 8)
  )) == false
)
