
def floodFill(src: String, dest: String,
              startX: Int, startY: Int,
              compareColors: (java.awt.Color, java.awt.Color) => Boolean,
              fillColor: java.awt.Color) =
  val raw = javax.imageio.ImageIO.read(java.io.File(src))
  val width = raw.getWidth
  val height = raw.getHeight
  val queue = collection.mutable.ArrayDeque((startX, startY))
  val max = width * height
  var n = 0
  val seen = collection.mutable.Set.empty[(Int, Int)]
  
  while queue.nonEmpty do
    val (currX, currY) = queue.removeHead()

    n += 1
    if n % 1000 == 0 then println(s"Filled $n/$max pixels")
    val currColor = java.awt.Color(raw.getRGB(currX, currY))
    raw.setRGB(currX, currY, fillColor.getRGB)

    def propagate(dx: Int, dy: Int) =
      val (nextX, nextY) = (currX + dx, currY + dy)

      if nextX >= 0 && nextY >= 0 && nextX < width && nextY < height then
        if !seen.contains((nextX, nextY)) then
          val nextColor = java.awt.Color(raw.getRGB(nextX, nextY))
          seen.add((nextX, nextY))
          if compareColors(currColor, nextColor) then queue.append((nextX, nextY))

    propagate(-1, 0)
    propagate(1, 0)
    propagate(0, -1)
    propagate(0, 1)

  javax.imageio.ImageIO.write(raw, "jpg", java.io.File(dest))
