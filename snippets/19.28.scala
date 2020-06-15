@ fastparse.parse("seven", number(_))
res54: Parsed[Int] = Success(Number(7), 5)

@ fastparse.parse("zero", number(_))
res55: Parsed[Int] = Success(Number(0), 4)

@ fastparse.parse("lol", number(_))
res56: Parsed[Int] = Parsed.Failure(Position 1:1, found "lol")
