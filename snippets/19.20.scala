> def prefix[T: P]: P[String] = P( "hello" | "goodbye" ).!

> def suffix[T: P]: P[String] = P( "world" | "seattle" ).!

> def ws[T: P]: P[Unit] = P( " ".rep(1) ) // white-space

> def parser[T: P] = P( prefix ~ ws ~ suffix ).map:
    case ("hello", place) => Phrase(true, place)
    case ("goodbye", place) => Phrase(false, place)

> val Parsed.Success(result, index) =
    fastparse.parse("goodbye   world", parser(using _)).runtimeChecked

val result: Phrase = Phrase(isHello = false, place = "world")
val index: Int = 15
