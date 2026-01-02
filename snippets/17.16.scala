   def main(src0: String, dest0: String): Unit =
     ...
     def pipelineCalls[T: upickle.Reader](rpcFor: os.SubPath => Option[Rpc]) = ...
+
+    val existsMap = pipelineCalls[Boolean](p => Some(Rpc.Exists(p)))
+    val isDirMap = pipelineCalls[Boolean](p => Some(Rpc.IsDir(p)))
+    val readMap = pipelineCalls[Array[Byte]]: p =>
+      if existsMap(p) && !isDirMap(p) then Some(Rpc.ReadBytes(p))
+      else None
+
+    pipelineCalls[Unit]: p =>
+      if os.isDir(src / p) then None
+      else
+        val localBytes = os.read.bytes(src / p)
+        if readMap.get(p).exists(java.util.Arrays.equals(_, localBytes)) then None
+        else Some(Rpc.WriteOver(localBytes, p))