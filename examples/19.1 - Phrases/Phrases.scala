//| mvnDeps:
//| - com.lihaoyi::fastparse:3.1.1
enum Phrase:
  case Word(s: String)
  case Pair(lhs: Phrase, rhs: Phrase)

import fastparse.*, NoWhitespace.*
def prefix[T: P] = P( "hello" | "goodbye" ).!.map(Phrase.Word(_))
def suffix[T: P] = P( "world" | "seattle" ).!.map(Phrase.Word(_))
def ws[T: P] = P( " ".rep(1) )
def parened[T: P] = P( "(" ~ parser ~ ")" )
def parser[T: P]: P[Phrase] = P(
  (parened | prefix) ~ ws ~ (parened | suffix)
).map:
  case (lhs, rhs) => Phrase.Pair(lhs, rhs)
