> def parser[T: P] = P(
    ("hello" | "goodbye").! ~ " ".rep(1) ~ ("world" | "seattle").! ~ End
  )

> fastparse.parse("hello seattle", parser(using _))
res29: fastparse.Parsed[(String, String)] = Success(
  value = ("hello", "seattle"),
  index = 13
)

> fastparse.parse("hello     world", parser(using _))
res30: fastparse.Parsed[(String, String)] = Success(
  value = ("hello", "world"),
  index = 15
)
