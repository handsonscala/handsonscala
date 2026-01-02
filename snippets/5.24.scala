> val s"$first $second" =
    "Hello World".runtimeChecked

first: String = "Hello"
second: String = "World"

> val flipped = s"$second $first"
flipped: String = "World Hello"

> val s"$first $second" =
    "Hello".runtimeChecked

scala.MatchError: Hello
