> def parser[T: P] = P( "hello" | "goodbye" )

> fastparse.parse("hello", parser(using _))
res6: fastparse.Parsed[Unit] = Success(value = (), index = 5)

> fastparse.parse("goodbye", parser(using _))
res7: fastparse.Parsed[Unit] = Success(value = (), index = 7)

> fastparse.parse("dunno", parser(using _))
res8: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:1, found "dunno")
