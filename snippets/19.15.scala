@ def parser[_: P] =
    P( ("hello" | "goodbye") ~ (" ".rep(1) ~ ("world" | "seattle")).? ~ End )
