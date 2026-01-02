> val Parsed.Failure(expected, index, extra) =
    fastparse.parse("dunno", parser(using _)).runtimeChecked

> println(extra.trace().longMsg)
Expected parser:1:1 / ("hello" | "goodbye"):1:1, found "dunno"
