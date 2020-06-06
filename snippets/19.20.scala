@ def prefix[_: P]: P[String] = P( "hello" | "goodbye" ).!

@ def suffix[_: P]: P[String] = P( "world" | "seattle" ).!

@ def ws[_: P]: P[Unit] = P( " ".rep(1) ) // white-space

@ def parser[_: P] = P( prefix ~ ws ~ suffix ).map{
    case ("hello", place) => new Phrase(true, place)
    case ("goodbye", place) => new Phrase(false, place)
  }

@ val Parsed.Success(result, index) = fastparse.parse("goodbye   world", parser(_))
result: Phrase = Phrase(false, "world")
index: Int = 15
