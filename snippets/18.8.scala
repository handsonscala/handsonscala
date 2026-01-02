   def main(src0: String, dest0: String): Unit =
     ...
+    enum Msg
+      case ChangedPath(value: os.SubPath)
+      case AgentResponse(value: Rpc.StatInfo)