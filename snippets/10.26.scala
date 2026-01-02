- val bootstrapCss = link(
-   rel := "stylesheet",
-   href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
- )
+ def bootstrap = Task:
+   os.write(
+     Task.dest / "bootstrap.css",
+     requests.get(
+       "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
+     )
+   )
+   PathRef(Task.dest / "bootstrap.css")