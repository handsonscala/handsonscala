   def main(args: Array[String]): Unit = {
     ...
+    sealed trait Msg
+    case class ChangedPath(value: os.SubPath) extends Msg
+    case class AgentResponse(value: Rpc.StatInfo) extends Msg
   }