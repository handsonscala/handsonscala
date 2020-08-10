@ def stdDev(a: Array[Double]): Double = {
    val mean = a.foldLeft(0.0)(_ + _) / a.length
    val squareErrors = a.map(_ - mean).map(x => x * x)
    math.sqrt(squareErrors.foldLeft(0.0)(_ + _) / a.length)
  }

@ stdDev(Array(1, 2, 3, 4, 5))
res29: Double = 1.4142135623730951

@ stdDev(Array(3, 3, 3))
res30: Double = 0.0
