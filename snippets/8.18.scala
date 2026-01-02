> given pathRw: upickle.ReadWriter[os.Path] =
    upickle.readwriter[String].bimap[os.Path](
      p => ... /* convert os.Path to String */,
      s => ... /* convert String to os.Path */
    )
