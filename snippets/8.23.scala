@ case class Asset(id: Int, name: String)

@ implicit val assetRw = upickle.default.macroRW[Asset]

@ def myPrintJson[T: upickle.default.Writer](t: T) = println(upickle.default.write(t))
