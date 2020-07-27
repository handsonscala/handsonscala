 cc.waitForInactivity()

-assert(os.read.lines(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
-assert(
-  os.read.lines(os.pwd / "log.txt") ==
-  Seq("I am cow, I am cow", "Hear me moo, moooo")
-)
+def decodeFile(p: os.Path) = {
+  os.read.lines(p).map(s => new String(java.util.Base64.getDecoder.decode(s)))
+}
+assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
+assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow, I am cow", "Hear me moo, moooo"))