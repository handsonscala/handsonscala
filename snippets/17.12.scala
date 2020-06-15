     val agent = os.proc(agentExecutable).spawn(cwd = dest)
+    def callAgent[T: upickle.default.Reader](rpc: Rpc): T = {
+      Shared.send(agent.stdin.data, rpc)
+      Shared.receive[T](agent.stdout.data)
+    }
     for (srcSubPath <- os.walk(src)) {