
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
