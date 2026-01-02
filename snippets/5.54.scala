def parseFromConsole[T](using parser: StrParser[T]) =
  parser.parse(scala.Console.in.readLine())

val myInt = parseFromConsole[Int]
