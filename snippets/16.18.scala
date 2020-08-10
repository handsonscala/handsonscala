 implicit val cc = new castor.Context.Test()

 val diskActor = new DiskActor(os.pwd / "log.txt", rotateSize = 50)
+val base64Actor = new Base64Actor(diskActor)
-val logger = diskActor
+val logger = base64Actor