
case class Point(x: Int, y: Int)

val p = Point(1, 2)

assert(p.x == 1)

assert(p.y == 2)

assert(p.toString == "Point(1,2)")

val p2 = Point(1, 2)

assert(p == p2)

val p3 = p.copy(y = 10)

assert(p3 == Point(1, 10))

val p4 = p3.copy(x = 20)

assert(p4 == Point(20, 10))
