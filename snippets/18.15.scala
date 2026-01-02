     enum Msg
     case ChangedPath(value: os.SubPath)
-      case AgentResponse(value: Rpc.StatInfo)
+      case HashStatInfo(localHash: Option[Int], value: Rpc.StatInfo)
     import castor.Context.Simple.global
     object SyncActor extends castor.SimpleActor[Msg]:
       def run(msg: Msg): Unit = msg match
         case ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
-        case AgentResponse(Rpc.StatInfo(p, remoteHash)) =>
-          val localHash = Shared.hashPath(src / p)
+        case HashStatInfo(localHash, Rpc.StatInfo(p, remoteHash)) =>
           if localHash != remoteHash && localHash.isDefined then
             Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))