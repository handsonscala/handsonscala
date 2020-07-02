+    object HashActor extends castor.SimpleActor[Rpc.StatInfo]{
+      def run(msg: Rpc.StatInfo): Unit = {
+        println("HashActor handling: " + msg)
+        val localHash = Shared.hashPath(src / msg.p)
+        SyncActor.send(HashStatInfo(localHash, msg))
+      }
+    }