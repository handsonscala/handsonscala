> fastparse.parse("hello     world", parser(using _))
res26: fastparse.Parsed[Unit] = Success(value = (), index = 15)

> fastparse.parse("hello", parser(using _))
res27: fastparse.Parsed[Unit] = Success(value = (), index = 5)
