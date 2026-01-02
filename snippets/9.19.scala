       html(
         body(
-          h1("Blog", " / ", suffix),
+          h1(a(href := "../index.html")("Blog"), " / ", suffix),
           raw(output)
         )
       )