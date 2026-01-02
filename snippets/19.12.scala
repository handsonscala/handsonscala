> fastparse.parse("hello world", parser(using _))
res15: fastparse.Parsed[Unit] = Success(value = (), index = 11)

> fastparse.parse("hello seattle", parser(using _))
res16: fastparse.Parsed[Unit] = Success(value = (), index = 13)

> fastparse.parse("goodbye world", parser(using _))
res17: fastparse.Parsed[Unit] = Success(value = (), index = 13)
