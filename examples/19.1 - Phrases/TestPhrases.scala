//| moduleDeps: [Phrases.scala]
import fastparse.*

def main() =
  assert(
    pprint.log(fastparse.parse("hello seattle", parser(using _))) ==
      Parsed.Success(value = Phrase.Pair(Phrase.Word("hello"), Phrase.Word("seattle")), index = 13)
  )
  assert(
    pprint.log(fastparse.parse("hello (goodbye seattle)", parser(using _))) ==
      Parsed.Success(
        value = Phrase.Pair(
          Phrase.Word("hello"),
          Phrase.Pair(Phrase.Word("goodbye"), Phrase.Word("seattle"))
        ),
        index = 23
      )
  )
  assert(
    pprint.log(fastparse.parse("(hello  world)   (goodbye seattle)", parser(using _))) ==
      Parsed.Success(
        value = Phrase.Pair(
          Phrase.Pair(Phrase.Word("hello"), Phrase.Word("world")),
          Phrase.Pair(Phrase.Word("goodbye"), Phrase.Word("seattle"))
        ),
        index = 34
      )
  )
  assert(
    pprint.log(fastparse.parse("(hello  world)   ((goodbye seattle) world)", parser(using _))) ==
      Parsed.Success(
        value = Phrase.Pair(
          Phrase.Pair(Phrase.Word("hello"), Phrase.Word("world")),
          Phrase.Pair(
            Phrase.Pair(Phrase.Word("goodbye"), Phrase.Word("seattle")),
            Phrase.Word("world")
          )
        ),
        index = 42
      )
  )
