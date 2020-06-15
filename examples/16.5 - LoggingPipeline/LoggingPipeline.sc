import $file.Classes, Classes._

implicit val cc = new castor.Context.Test()

val diskActor = new DiskActor(os.pwd / "log.txt")
val base64Actor = new Base64Actor(diskActor)

val logger = base64Actor
