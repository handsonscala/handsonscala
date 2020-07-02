@ fastparse.parse("hello", parser(_))
res2: Parsed[Unit] = Success((), 5)

@ fastparse.parse("goodbye", parser(_))
res3: Parsed[Unit] = Parsed.Failure(Position 1:1, found "goodbye")
