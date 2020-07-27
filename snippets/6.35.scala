floodFill(
  src = "Raw.jpg", dest = "Filled.jpg", startX = 180, startY = 90,
  compareColors = { (a: java.awt.Color, b: java.awt.Color) =>
    def sqrDiff(f: java.awt.Color => Int) = math.pow(f(a) - f(b), 2)
    math.sqrt(sqrDiff(_.getBlue) + sqrDiff(_.getGreen) + sqrDiff(_.getRed)) < 25
  },
  fillColor = java.awt.Color.BLACK
)