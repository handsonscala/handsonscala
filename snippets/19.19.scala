> case class Phrase(isHello: Boolean, place: String)

> def parser[T: P]: P[Phrase] = P(
    ("hello" | "goodbye").! ~ " ".rep(1) ~ ("world" | "seattle").! ~ End
  ).map:
    case ("hello", place) => Phrase(true, place)
    case ("goodbye", place) => Phrase(false, place)

> val Parsed.Success(result, index) =
    fastparse.parse("goodbye   seattle", parser(using _)).runtimeChecked

> result.isHello
res31: Boolean = false

> result.place
res32: String = "seattle"
