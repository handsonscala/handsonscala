> val Parsed.Failure(expected, index, extra) =
    fastparse.parse("goodbye", parser(using _)).runtimeChecked

> println(extra.trace().longMsg)
Expected parser:1:1 / "hello":1:1, found "goodbye"
