> val result = fastparse.parse("(two plus ten) times seven", parser(using _))
result: fastparse.Parsed[Expr] = Parsed.Failure(Position 1:1, found "(two plus ")

> val Parsed.Failure(msg, idx, extra) = result.runtimeChecked

> println(extra.trace().msg)
Expected ("(" ~ parser | number):1:1, found "(two plus "
