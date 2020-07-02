@ val result = fastparse.parse("(two plus ten) times seven", parser(_))
result: Parsed[Int] = Parsed.Failure(Position 1:11, found "ten) times")

@ val Parsed.Failure(msg, idx, extra) = result

@ println(extra.trace().msg)
Expected ("(" | number):1:11, found "ten) times"
