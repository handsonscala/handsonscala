import $file.Classes, Classes._

implicit val cc = new castor.Context.Test()

val diskActor = new DiskActor(os.pwd / "log.txt")
val uploadActor = new UploadActor("https://httpbin.org/post")
val base64Actor = new Base64Actor(new castor.SplitActor(diskActor, uploadActor))

val logger = base64Actor