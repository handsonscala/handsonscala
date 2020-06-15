ctx.run(
  query[Message].insert(
    _.parent -> lift(parentInt), _.name -> lift(name), _.msg -> lift(msg)
  )
)
