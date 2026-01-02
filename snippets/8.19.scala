> given pathRw: upickle.ReadWriter[os.Path] =
    upickle.readwriter[String].bimap[os.Path](
      p => p.toString,
      s => os.Path(s)
    )
