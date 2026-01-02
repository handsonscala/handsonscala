 given cc: castor.Context.Test()

 val diskActor = DiskActor(os.pwd / "log.txt")
-val base64Actor = Base64Actor(diskActor)
-val logger = base64Actor
+val uploadActor = UploadActor("https://httpbin.org/post")
+val base64Actor = Base64Actor(castor.SplitActor(diskActor, uploadActor))
+val sanitizeActor = SanitizeActor(base64Actor)
+val logger = sanitizeActor