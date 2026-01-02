> def parser[T: P] = P(
    ("hello" | "goodbye") ~ " ".rep(1) ~ ("world" | "seattle") ~ End
  )

> fastparse.parse("hello world", parser(using _))
res23: fastparse.Parsed[Unit] = Success(value = (), index = 11)

> fastparse.parse("hello     world", parser(using _))
res24: fastparse.Parsed[Unit] = Success(value = (), index = 15)

> fastparse.parse("helloworld", parser(using _))
res25: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:6, found "world")
