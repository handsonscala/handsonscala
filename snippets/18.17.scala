     val agentReader = Thread(() =>
       while agent.isAlive() do
-        SyncActor.send(AgentResponse(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
+        HashActor.send(Shared.receive[Rpc.StatInfo](agent.stdout.data))
     )