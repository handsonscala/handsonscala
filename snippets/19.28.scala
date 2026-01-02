> fastparse.parse("seven", number(using _))
res37: fastparse.Parsed[Expr] = Success(value = Number(7), index = 5)

> fastparse.parse("zero", number(using _))
res38: fastparse.Parsed[Expr] = Success(value = Number(0), index = 4)

> fastparse.parse("lol", number(using _))
res39: fastparse.Parsed[Expr] = Parsed.Failure(Position 1:1, found "lol")
