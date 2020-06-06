@ fastparse.parse("one plus two", parser(_))
+parser:1:1, cut
  +expr:1:1, cut
    +number:1:1
    -number:1:1:Success(1:4)
  -expr:1:1:Success(1:4, cut)
  +operator:1:5, cut
  -operator:1:5:Success(1:9, cut)
  +expr:1:10, cut
    +number:1:10
    -number:1:10:Success(1:13)
  -expr:1:10:Success(1:13, cut)
-parser:1:1:Success(1:13, cut)
res77: Parsed[Int] = Success(BinOp(Number(1), "plus", Number(2)), 12)
