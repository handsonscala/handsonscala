@ def parser[_: P] = P( ("hello" | "goodbye") ~ " ".rep(1) ~ ("world" | "seattle") ~ End )

@ fastparse.parse("hello world", parser(_))
res28: Parsed[Unit] = Success((), 11)

@ fastparse.parse("hello     world", parser(_))
res29: Parsed[Unit] = Success((), 15)

@ fastparse.parse("helloworld", parser(_))
res30: Parsed[Unit] = Parsed.Failure(Position 1:6, found "world")
