 logger.send("Comes from liquids from my udder")
-logger.send("I am cow, I am cow")
+logger.send("I am cow1234567887654321")
 logger.send("Hear me moo, moooo")

 cc.waitForInactivity()

 def decodeFile(p: os.Path) = {
   os.read.lines(p).map(s => new String(java.util.Base64.getDecoder.decode(s)))
 }
-assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
-assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow, I am cow", "Hear me moo, moooo"))
+assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
+assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow<redacted>", "Hear me moo, moooo"))