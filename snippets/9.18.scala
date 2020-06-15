     html(
       body(
         h1("Blog"),
         for ((_, suffix, _) <- postInfo)
-        yield h2(suffix)
+        yield h2(a(href := ("post/" + mdNameToHtml(suffix)), suffix))
       )
     )