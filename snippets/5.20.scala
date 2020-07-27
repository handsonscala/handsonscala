@ val s"$first $second" = "Hello World"
first: String = "Hello"
second: String = "World"

@ val flipped = s"$second $first"
flipped: String = "World Hello"

@ val s"$first $second" = "Hello"
scala.MatchError: Hello
