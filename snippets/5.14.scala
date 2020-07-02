@ case class Point(x: Int, y: Int)

@ def direction(p: Point) = p match {
    case Point(0, 0) => "origin"
    case Point(_, 0) => "horizontal"
    case Point(0, _) => "vertical"
    case _ => "diagonal"
  }

@ direction(Point(0, 0))
res28: String = "origin"

@ direction(Point(1, 1))
res29: String = "diagonal"

@ direction(Point(10, 0))
res30: String = "horizontal"
