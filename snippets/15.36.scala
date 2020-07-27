query[Message]
  .insert(_.parent -> lift(p), _.name -> lift(n), _.msg -> lift(m))
