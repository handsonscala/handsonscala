           div(
-            p(b("alice"), " ", "Hello World!"),
-            p(b("bob"), " ", "I am cow, hear me moo"),
+            for ((name, msg) <- messages) yield p(b(name), " ", msg)
           ),