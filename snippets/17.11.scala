     val agent = os.spawn(cmd = agentExecutable, cwd = dest)
+    def callAgent[T: upickle.Reader](rpc: Rpc): T =
+      Shared.send(agent.stdin.data, rpc)
+      Shared.receive[T](agent.stdout.data)
+
     for srcSubPath <- os.walk(src) do