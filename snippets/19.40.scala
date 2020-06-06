@ fastparse.parse("(two plus ten) times seven", parser(_))
+parser:1:1, cut
  +expr:1:1, cut
    +parser:1:2
      +expr:1:2
        +number:1:2
        -number:1:2:Success(1:5)
      -expr:1:2:Success(1:5)
      +operator:1:6
      -operator:1:6:Success(1:10)
      +expr:1:11
        +number:1:11
        -number:1:11:Failure(number:1:11 / ("zero" | "one" ...):1:11 ..."ten) times")
      -expr:1:11:Failure(expr:1:11 / ("(" | number):1:11 ..."ten) times")
    -parser:1:2:Failure(parser:1:2 / expr:1:11 / ("(" | number):1:11 ..."two plus t")
    +number:1:1
    -number:1:1:Failure(number:1:1 / ("zero" | "one" | "two" ...):1:1 ..."(two plus ")
  -expr:1:1:Failure(expr:1:1 / ("(" ~ parser | number):1:1 ..."(two plus ", cut)
-parser:1:1:Failure(parser:1:1 / expr:1:1 / ("(" ~ parser | number):1:1 ..."(two plus ")
