import $file.Phrases, Phrases._
import fastparse._

assert(
  pprint.log(fastparse.parse("hello seattle", parser(_))) ==
    Parsed.Success(value = Pair(Word("hello"), Word("seattle")), index = 13)
)
assert(
  pprint.log(fastparse.parse("hello (goodbye seattle)", parser(_))) ==
    Parsed.Success(
      value = Pair(Word("hello"), Pair(Word("goodbye"), Word("seattle"))),
      index = 23
    )
)
assert(
  pprint.log(fastparse.parse("(hello  world)   (goodbye seattle)", parser(_))) ==
    Parsed.Success(
      value = Pair(Pair(Word("hello"), Word("world")), Pair(Word("goodbye"), Word("seattle"))),
      index = 34
    )
)
assert(
  pprint.log(fastparse.parse("(hello  world)   ((goodbye seattle) world)", parser(_))) ==
    Parsed.Success(
      value = Pair(Pair(Word("hello"), Word("world")), Pair(Pair(Word("goodbye"), Word("seattle")), Word("world"))),
      index = 42
    )
)
