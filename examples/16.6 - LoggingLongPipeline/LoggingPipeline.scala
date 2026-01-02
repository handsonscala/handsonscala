//| moduleDeps: [Classes.scala]
given cc: castor.Context.Test()

val diskActor = DiskActor(os.pwd / "log.txt")
val uploadActor = UploadActor("https://httpbin.org/post")
val base64Actor = Base64Actor(castor.SplitActor(diskActor, uploadActor))
val sanitizeActor = SanitizeActor(base64Actor)

val logger = sanitizeActor
