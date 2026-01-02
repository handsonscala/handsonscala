 object sync extends SyncModule:
   def moduleDeps = Seq(shared)
+  def resources = Task:
+    os.copy(agent.assembly().path, Task.dest / "agent.jar")
+    super.resources() ++ Seq(PathRef(Task.dest))