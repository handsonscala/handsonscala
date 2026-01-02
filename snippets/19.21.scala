> def parser[T: P] = P( prefix ~ ws ~ suffix ).map:
    case ("hello", spaces, place) => Phrase(true, place)
    case ("goodbye", spaces, place) => Phrase(false, place)
-- [E007] Type Mismatch Error: -------------------------------------------------
2 |  case ("hello", spaces, place) => Phrase(true, place)
  |                                                    ^^^^^
  |                                                   Found:    (place : Any)
  |                                                   Required: String

> // second attempt to diagnose - don't use wrongly-typed `place` as argument
  def parser[T: P] = P( prefix ~ ws ~ suffix ).map:
    case ("hello", spaces, place) => Phrase(true, ???)
    case ("goodbye", spaces, place) => Phrase(false, ???)
-- [E029] Pattern Match Exhaustivity Warning: ----------------------------------
2 |  case ("hello", spaces, place) => Phrase(true, ???)
  |  ^
  |  match may not be exhaustive.
  |
  |  It would fail on pattern case: (_, _)
-- Error: ----------------------------------------------------------------------
2 |  case ("hello", spaces, place) => Phrase(true, ???)
  |       ^
  |this case is unreachable since type (String, String) is not a subclass of class Tuple3
