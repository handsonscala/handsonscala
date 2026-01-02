> fastparse.parse("hello", parser(using _))
res2: fastparse.Parsed[Unit] = Success(value = (), index = 5)

> fastparse.parse("goodbye", parser(using _))
res3: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:1, found "goodbye")
