def main() =
  val raw = javax.imageio.ImageIO.read(java.io.File("Raw.jpg"))
  val (x, y) = (90, 180)
  val color = java.awt.Color(raw.getRGB(x, y))
  val (r, g, b) = (color.getRed, color.getGreen, color.getBlue)
  raw.setRGB(x, y, java.awt.Color.BLACK.getRGB)
  javax.imageio.ImageIO.write(raw, "jpg", java.io.File("Out.jpg"))