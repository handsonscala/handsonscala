@ fastparse.parse("hello world", parser(_))
res19: Parsed[Unit] = Success((), 11)

@ fastparse.parse("hello seattle", parser(_))
res20: Parsed[Unit] = Success((), 13)

@ fastparse.parse("goodbye world", parser(_))
res21: Parsed[Unit] = Success((), 13)
