> case class Asset(id: Int, name: String) derives upickle.ReadWriter

> def myPrintJson[T: upickle.Writer](t: T) = println(upickle.write(t))
