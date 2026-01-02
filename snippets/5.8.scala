> enum Color(val rgb: Int, val chineseName: String):
    case Red   extends Color(0xFF0000, "红")
    case Green extends Color(0x00FF00, "绿")
    case Blue  extends Color(0x0000FF, "蓝")

> val myColors = Seq(Color.Red, Color.Green, Color.Blue, Color.Red, Color.Green)

> myColors.map(_.rgb.toHexString).mkString(" ")
res3: String = "ff0000 ff00 ff ff0000 ff00"

> myColors.map(_.chineseName).mkString(" ")
res2: String = "红 绿 蓝 红 绿"
