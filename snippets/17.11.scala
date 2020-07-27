     val src = os.Path(args(0), os.pwd)
     val dest = os.Path(args(1), os.pwd)
+    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
+    os.perms.set(agentExecutable, "rwx------")
+    val agent = os.proc(agentExecutable).spawn(cwd = dest)
     for (srcSubPath <- os.walk(src)) {
       val subPath = srcSubPath.subRelativeTo(src)
       val destSubPath = dest / subPath