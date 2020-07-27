@ def parser[_: P] = P( "hello" | "goodbye" )

@ fastparse.parse("hello", parser(_))
res7: Parsed[Unit] = Success((), 5)

@ fastparse.parse("goodbye", parser(_))
res8: Parsed[Unit] = Success((), 7)

@ fastparse.parse("dunno", parser(_))
res9: Parsed[Unit] = Parsed.Failure(Position 1:1, found "dunno")
