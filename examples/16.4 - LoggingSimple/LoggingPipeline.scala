//| moduleDeps: [Classes.scala]
given cc: castor.Context.Test()

val diskActor = DiskActor(os.pwd / "log.txt")

val logger = diskActor
