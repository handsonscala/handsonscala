   @cask.get("/")
-  def hello(errorOpt: Option[String] = None) = doctype("html")(
+  def hello(errorOpt: Option[String] = None,
+            userName: Option[String] = None,
+            msg: Option[String] = None) = doctype("html")(
     html(