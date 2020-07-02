@ implicit val pathRw = upickle.default.readwriter[String].bimap[os.Path](
    p => p.toString,
    s => os.Path(s)
  )
