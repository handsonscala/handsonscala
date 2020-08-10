           div(for ((name, msg) <- messages) yield p(b(name), " ", msg)),
+          for (error <- errorOpt) yield i(color.red)(error),
           form(action := "/", method := "post")(