> def parser[T: P] = P( "hello" ~ "goodbye" )

> fastparse.parse("hellogoodbye", parser(using _))
res9: fastparse.Parsed[Unit] = Success(value = (), index = 12)

> fastparse.parse("hello", parser(using _))
res10: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:6, found "")

> fastparse.parse("goodbye", parser(using _))
res11: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:1, found "goodbye")
