def alpha[_: P] = P( CharIn("a-z") )
def nocut[_: P] = P( "val " ~ alpha.rep(1).! | "def " ~ alpha.rep(1).!)

val Parsed.Success("abcd", _) = parse("val abcd", nocut(_))
val failure = parse("val 1234", nocut(_)).asInstanceOf[Parsed.Failure]
val trace = failure.trace().longAggregateMsg
failure.index // 0
trace //  Expected nocut:1:1 / ("val " ~ alpha.rep(1) | "def "):1:1, found "val 1234"
