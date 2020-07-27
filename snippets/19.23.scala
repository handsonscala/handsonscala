@ {
  sealed trait Phrase
  case class Word(s: String) extends Phrase
  case class Pair(lhs: Phrase, rhs: Phrase) extends Phrase
  }
