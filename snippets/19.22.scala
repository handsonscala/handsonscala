@ fastparse.parse("hello", prefix(_))
res46: Parsed[String] = Success("hello", 5)

@ fastparse.parse("goodbye", prefix(_))
res47: Parsed[String] = Success("goodbye", 7)

@ fastparse.parse("moo", prefix(_))
res48: Parsed[String] = Parsed.Failure(Position 1:1, found "moo")
