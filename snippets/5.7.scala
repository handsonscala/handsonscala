> enum Color:
    case Red, Green, Blue

> val myColors = Seq(Color.Red, Color.Green, Color.Blue, Color.Red, Color.Green)

> myColors.count(_ != Color.Red)
res1: Int = 3
