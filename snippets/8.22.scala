@ implicit val pathRw = upickle.default.readwriter[ujson.Value].bimap[Thing](
    p => ... /* convert a Thing to ujson.Value */,
    s => ... /* convert a ujson.Value to Thing */
  )
