//| moduleDeps: [Classes.scala]
given cc: castor.Context.Test()

val diskActor = DiskActor(os.pwd / "log.txt")
val base64Actor = Base64Actor(diskActor)

val logger = base64Actor
