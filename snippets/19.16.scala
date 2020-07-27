@ fastparse.parse("hello     world", parser(_))
res31: Parsed[Unit] = Success((), 15)

@ fastparse.parse("hello", parser(_))
res32: Parsed[Unit] = Success((), 5)
