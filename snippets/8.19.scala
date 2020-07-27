@ implicit val pathRw = upickle.default.readwriter[String].bimap[os.Path](
    p => ... /* convert os.Path to String */,
    s => ... /* convert String to os.Path */
  )
