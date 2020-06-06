
def renderSudoku(grid: Array[Array[Int]]) = {
  val rowSeparator = "\n+-------+-------+-------+\n"
  grid
    .map(row =>
      row
        .map(i => if(i == 0) " " else i.toString)
        .grouped(3).map(_.mkString(" "))
        .mkString("| ", " | ", " |")
    )
    .grouped(3)
    .map(_.mkString("\n"))
    .mkString(rowSeparator, rowSeparator, rowSeparator)
}

val expected = """
+-------+-------+-------+
| 3 1 6 | 5 7 8 | 4 9 2 |
| 5 2 9 | 1 3 4 | 7 6 8 |
| 4 8 7 | 6 2 9 | 5 3 1 |
+-------+-------+-------+
| 2 6 3 |   1   |   8   |
| 9 7 4 | 8 6 3 |     5 |
| 8 5 1 |   9   | 6     |
+-------+-------+-------+
| 1 3   |       | 2 5   |
|       |       |   7 4 |
|     5 | 2   6 | 3     |
+-------+-------+-------+
"""
val grid = Array(
  Array(3, 1, 6,   5, 7, 8,   4, 9, 2),
  Array(5, 2, 9,   1, 3, 4,   7, 6, 8),
  Array(4, 8, 7,   6, 2, 9,   5, 3, 1),

  Array(2, 6, 3,   0, 1, 0,   0, 8, 0),
  Array(9, 7, 4,   8, 6, 3,   0, 0, 5),
  Array(8, 5, 1,   0, 9, 0,   6, 0, 0),

  Array(1, 3, 0,   0, 0, 0,   2, 5, 0),
  Array(0, 0, 0,   0, 0, 0,   0, 7, 4),
  Array(0, 0, 5,   2, 0, 6,   3, 0, 0)
)

val rendered = renderSudoku(grid)
pprint.log(rendered)
assert(rendered == expected)
