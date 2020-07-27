@ def parser[_: P] =
    P( ("hello" | "goodbye").! ~ " ".rep(1) ~ ("world" | "seattle").! ~ End )

@ fastparse.parse("hello seattle", parser(_))
res41: Parsed[(String, String)] = Success(("hello", "seattle"), 13)

@ fastparse.parse("hello     world", parser(_))
res42: Parsed[(String, String)] = Success(("hello", "world"), 15)
