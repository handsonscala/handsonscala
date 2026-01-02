         div(cls := "container")(
-          h1("Hello!"),
-          p("World")
+          h1("Scala Chat!"),
+          div(
+            p(b("alice"), " ", "Hello World!"),
+            p(b("bob"), " ", "I am cow, hear me moo")
+          ),
+          div(
+            input(`type` := "text", placeholder := "User name"),
+            input(`type` := "text", placeholder := "Write a message!")
+          )
         )