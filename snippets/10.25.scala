- val bootstrapCss = link(
-   rel := "stylesheet",
-   href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
- )
+ def bootstrap = T{
+   os.write(
+     T.dest / "bootstrap.css",
+     requests.get("https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css")
+       .text()
+   )
+   PathRef(T.dest / "bootstrap.css")
+ }