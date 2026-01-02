//| moduleDeps: [Classes.scala]
given cc: castor.Context.Test()

val diskActor = DiskActor(os.pwd / "log.txt")
val uploadActor = UploadActor("https://httpbin.org/post")

val base64Actor = Base64Actor(diskActor)
val sanitizeActor = SanitizeActor(uploadActor)

val logger = castor.SplitActor(base64Actor, sanitizeActor)
