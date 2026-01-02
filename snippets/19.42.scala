def alpha[T: P] = P( CharIn("a-z") )
def hascuts[T: P] = P( "val " ~/ alpha.rep(1).! | "def " ~/ alpha.rep(1).!)

val Parsed.Success("abcd", _) = parse("val abcd", hascuts(using _)).runtimeChecked
val failure = parse("val 1234", hascuts(using _)).asInstanceOf[Parsed.Failure]
val trace = failure.trace().longAggregateMsg
failure.index // 4
trace // Expected hascuts:1:1 / alpha:1:5 / [a-z]:1:5, found "1234"
