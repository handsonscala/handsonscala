@ def stdDev(a: Array[Double]): Double = {
    val mean = a.sum / a.length
    val squareErrors = a.map(_ - mean).map(x => x * x)
    math.sqrt(squareErrors.sum / a.length)
  }
