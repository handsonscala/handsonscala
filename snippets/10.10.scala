 import mill.*
+trait FooModule extends Module:
   def srcs = Task.Source("src")
   def resources = Task.Source("resources")

   def concat = Task:
     ...

   def compress = Task:
     ...

   def zipped = Task:
     ...
+
+object bar extends FooModule
+object qux extends FooModule