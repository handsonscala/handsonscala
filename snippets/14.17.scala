   @cask.get("/")
-  def hello() = doctype("html")(
+  def hello(errorOpt: Option[String] = None) = doctype("html")(
     html(