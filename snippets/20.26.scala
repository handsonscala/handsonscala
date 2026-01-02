> fastparse.parse("""{"a": "b", "cde": id, "nested": {}}""", expr(using _))
res8: fastparse.Parsed[Expr] = Parsed.Failure(Position 1:1, found "{\"a\": \"b\",")
