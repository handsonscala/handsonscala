> def parser[T: P] = P( "hello" ~ "goodbye" ~ End )

> fastparse.parse("hellogoodbye", parser(using _))
res12: fastparse.Parsed[Unit] = Success(value = (), index = 12)

> fastparse.parse("hellogoodbyeworld", parser(using _))
res13: fastparse.Parsed[Unit] = Parsed.Failure(Position 1:13, found "world")

> val Parsed.Failure(msg, idx, extra) =
    fastparse.parse("hellogoodbyeworld", parser(using _)).runtimeChecked

> extra.trace().longMsg
res14: String = "Expected parser:1:1 / end-of-input:1:13, found \"world\""
