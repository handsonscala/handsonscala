> fastparse.parse("hello universe", parser(using _)) // Not "world" or "seattle"
res18: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:7, found "universe")

> fastparse.parse("helloworld", parser(using _)) // Missing required " " blank space
res19: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:6, found "world")

> fastparse.parse("hello  world", parser(using _)) // Too many blank spaces
res20: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:7, found " world")

> fastparse.parse("i love seattle", parser(using _)) // Not a hello or goodbye
res21: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:1, found "i love sea")

> fastparse.parse("hello seattle moo", parser(using _)) // unexpected extra text
res22: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:14, found " moo")
