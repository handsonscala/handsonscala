     val agentReader = new Thread(() => {
       while (agent.isAlive()) {
-        SyncActor.send(AgentResponse(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
+        HashActor.send(Shared.receive[Rpc.StatInfo](agent.stdout.data))
       }
     })