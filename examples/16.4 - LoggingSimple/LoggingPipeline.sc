import $file.Classes, Classes._

implicit val cc = new castor.Context.Test()

val diskActor = new DiskActor(os.pwd / "log.txt")

val logger = diskActor
