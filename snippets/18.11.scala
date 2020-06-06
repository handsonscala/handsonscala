   def main(args: Array[String]): Unit = {
     ...
+    val agentReader = new Thread(() => {
+      while (agent.isAlive()) {
+        SyncActor.send(AgentResponse(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
+      }
+    })
+    agentReader.start()
+    val watcher = os.watch.watch(
+      Seq(src),
+      onEvent = _.foreach(p => SyncActor.send(ChangedPath(p.subRelativeTo(src))))
+    )
+    Thread.sleep(Long.MaxValue)
   }