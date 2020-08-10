 os.write(
   os.pwd / "out" / "index.html",
   doctype("html")(
     html(
+      head(bootstrapCss),
       body(
         h1("Blog"),