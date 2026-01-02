> def stdDev(a: Array[Double]): Double =
    val mean = a.sum / a.length
    val squareErrors = a.map(x => x - mean).map(x => x * x)
    math.sqrt(squareErrors.sum / a.length)


> stdDev(Array(1, 2, 3))
res0: Double = 0.816496580927726
