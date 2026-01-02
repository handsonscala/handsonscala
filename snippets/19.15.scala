> def parser[T: P] = P(
    ("hello" | "goodbye") ~ (" ".rep(1) ~ ("world" | "seattle")).? ~ End
  )
