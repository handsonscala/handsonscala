def alpha[_: P] = P( CharIn("a-z") )
def hascuts[_: P] = P( "val " ~/ alpha.rep(1).! | "def " ~/ alpha.rep(1).!)

val Parsed.Success("abcd", _) = parse("val abcd", hascuts(_))
val failure = parse("val 1234", hascuts(_)).asInstanceOf[Parsed.Failure]
val trace = failure.trace().longAggregateMsg
failure.index // 4
trace // Expected hascuts:1:1 / alpha:1:5 / [a-z]:1:5, found "1234"
