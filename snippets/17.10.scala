 object sync extends SyncModule{
   def moduleDeps = Seq(shared)
+  def resources = T.sources{
+    os.copy(agent.assembly().path, T.dest / "agent.jar")
+    super.resources() ++ Seq(PathRef(T.dest))
+  }
 }