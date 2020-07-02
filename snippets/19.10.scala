@ def parser[_: P] = P( "hello" ~ "goodbye" )

@ fastparse.parse("hellogoodbye", parser(_))
res11: Parsed[Unit] = Success((), 12)

@ fastparse.parse("hello", parser(_))
res12: Parsed[Unit] = Parsed.Failure(Position 1:6, found "")

@ fastparse.parse("goodbye", parser(_))
res13: Parsed[Unit] = Parsed.Failure(Position 1:1, found "goodbye")
