> fastparse.parse("one plus two", parser(using _))
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
res43: fastparse.Parsed[Expr] = Success(
  value = BinOp(left = Number(1), op = "plus", right = Number(2)),
  index = 12
)
