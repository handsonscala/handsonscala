@ def parser[_: P] = P( "hello" ~ "goodbye" ~ End )

@ fastparse.parse("hellogoodbye", parser(_))
res15: Parsed[Unit] = Success((), 12)

@ fastparse.parse("hellogoodbyeworld", parser(_))
res16: Parsed[Unit] = Parsed.Failure(Position 1:13, found "world")

@ val Parsed.Failure(msg, idx, extra) = fastparse.parse("hellogoodbyeworld", parser(_))

@ extra.traced.longMsg
res17: String = "Expected parser:1:1 / end-of-input:1:13, found \"world\""
