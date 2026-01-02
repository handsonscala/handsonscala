> fastparse.parse("hello", prefix(using _))
res33: fastparse.Parsed[String] = Success(value = "hello", index = 5)

> fastparse.parse("goodbye", prefix(using _))
res34: fastparse.Parsed[String] = Success(value = "goodbye", index = 7)

> fastparse.parse("moo", prefix(using _))
res35: fastparse.Parsed[String] = Parsed.Failure(Position 1:1, found "moo")
