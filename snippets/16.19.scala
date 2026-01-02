 given cc: castor.Context.Test()

 val diskActor = DiskActor(os.pwd / "log.txt", rotateSize = 50)
-val logger = diskActor
+val base64Actor = Base64Actor(diskActor)
+val logger = base64Actor