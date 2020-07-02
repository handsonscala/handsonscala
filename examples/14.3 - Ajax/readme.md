# Example 14.3 - Ajax
Ajax-based interactive chat website

```bash
./mill -i app.test
```


## Upstream Example: [14.2 - Forms](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.2%20-%20Forms):
Diff:
```diff
diff --git a/14.3 - Ajax/app/resources/static/app.js b/14.3 - Ajax/app/resources/static/app.js
new file mode 100644
index 0000000..333d200
--- /dev/null
+++ b/14.3 - Ajax/app/resources/static/app.js	
@@ -0,0 +1,13 @@
+function submitForm() {
+  fetch(
+    "/",
+    {method: "POST", body: JSON.stringify({name: nameInput.value, msg: msgInput.value})}
+  ).then(response => response.json())
+   .then(json => {
+    if (json["success"]) {
+      messageList.innerHTML = json["txt"]
+      msgInput.value = ""
+    }
+    errorDiv.innerText = json["err"]
+  })
+}
\ No newline at end of file
diff --git a/14.2 - Forms/app/src/MinimalApplication.scala b/14.3 - Ajax/app/src/MinimalApplication.scala
index 7035989..01e7963 100644
--- a/14.2 - Forms/app/src/MinimalApplication.scala	
+++ b/14.3 - Ajax/app/src/MinimalApplication.scala	
@@ -4,30 +4,24 @@ object MinimalApplication extends cask.MainRoutes {
   var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
 
+  @cask.staticResources("/static")
+  def staticResourceRoutes() = "static"
+
   @cask.get("/")
-  def hello(errorOpt: Option[String] = None,
-            userName: Option[String] = None,
-            msg: Option[String] = None) = doctype("html")(
+  def hello() = doctype("html")(
     html(
-      head(link(rel := "stylesheet", href := bootstrap)),
+      head(
+        link(rel := "stylesheet", href := bootstrap),
+        script(src := "/static/app.js")
+      ),
       body(
         div(cls := "container")(
           h1("Scala Chat!"),
-          div(for ((name, msg) <- messages) yield p(b(name), " ", msg)),
-          for (error <- errorOpt) yield i(color.red)(error),
-          form(action := "/", method := "post")(
-            input(
-              `type` := "text",
-              name := "name",
-              placeholder := "User name",
-              userName.map(value := _)
-            ),
-            input(
-              `type` := "text",
-              name := "msg",
-              placeholder := "Write a message!",
-              msg.map(value := _)
-            ),
+          div(id := "messageList")(messageList()),
+          div(id := "errorDiv", color.red),
+          form(onsubmit := "submitForm(); return false")(
+            input(`type` := "text", id := "nameInput", placeholder := "User name"),
+            input(`type` := "text", id := "msgInput", placeholder := "Write a message!"),
             input(`type` := "submit")
           )
         )
@@ -35,13 +29,15 @@ object MinimalApplication extends cask.MainRoutes {
     )
   )
 
-  @cask.postForm("/")
+  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))
+
+  @cask.postJson("/")
   def postChatMsg(name: String, msg: String) = {
-    if (name == "") hello(Some("Name cannot be empty"), Some(name), Some(msg))
-    else if (msg == "") hello(Some("Message cannot be empty"), Some(name), Some(msg))
+    if (name == "") ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
+    else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else {
       messages = messages :+ (name -> msg)
-      hello(None, Some(name), None)
+      ujson.Obj("success" -> true, "txt" -> messageList().render, "err" -> "")
     }
   }
 
diff --git a/14.2 - Forms/app/test/src/ExampleTests.scala b/14.3 - Ajax/app/test/src/ExampleTests.scala
index feb6e49..112a9f6 100644
--- a/14.2 - Forms/app/test/src/ExampleTests.scala	
+++ b/14.3 - Ajax/app/test/src/ExampleTests.scala	
@@ -5,12 +5,12 @@ import utest._
 object ExampleTests extends TestSuite {
   def withServer[T](example: cask.main.Main)(f: String => T): T = {
     val server = io.undertow.Undertow.builder
-      .addHttpListener(8082, "localhost")
+      .addHttpListener(8083, "localhost")
       .setHandler(example.defaultHandler)
       .build
     server.start()
     val res =
-      try f("http://localhost:8082")
+      try f("http://localhost:8083")
       finally server.stop()
     res
   }
@@ -26,24 +26,38 @@ object ExampleTests extends TestSuite {
       assert(success.text().contains("I am cow, hear me moo"))
       assert(success.statusCode == 200)
 
-      val response = requests.post(host, data = Map("name" -> "haoyi", "msg" -> "Test Message!"))
+      val response = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> "Test Message!"))
 
-      assert(response.text().contains("Scala Chat!"))
-      assert(response.text().contains("alice"))
-      assert(response.text().contains("Hello World!"))
-      assert(response.text().contains("bob"))
-      assert(response.text().contains("I am cow, hear me moo"))
-      assert(response.text().contains("haoyi"))
-      assert(response.text().contains("Test Message!"))
+      val parsed = ujson.read(response.text())
+      assert(parsed("success") == ujson.True)
+      assert(parsed("err") == ujson.Str(""))
+
+      val parsedTxt = parsed("txt").str
+      assert(parsedTxt.contains("alice"))
+      assert(parsedTxt.contains("Hello World!"))
+      assert(parsedTxt.contains("bob"))
+      assert(parsedTxt.contains("I am cow, hear me moo"))
+      assert(parsedTxt.contains("haoyi"))
+      assert(parsedTxt.contains("Test Message!"))
       assert(response.statusCode == 200)
     }
     test("failure") - withServer(MinimalApplication) { host =>
-      val response1 = requests.post(host, data = Map("name" -> "haoyi"), check = false)
+      val response1 = requests.post(host, data = ujson.Obj("name" -> "haoyi"), check = false)
       assert(response1.statusCode == 400)
-      val response2 = requests.post(host, data = Map("name" -> "haoyi", "msg" -> ""))
-      assert(response2.text().contains("Message cannot be empty"))
-      val response3 = requests.post(host, data = Map("name" -> "", "msg" -> "Test Message!"))
-      assert(response3.text().contains("Name cannot be empty"))
+      val response2 = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> ""))
+      assert(
+        ujson.read(response2.text()) ==
+        ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
+      )
+      val response3 = requests.post(host, data = ujson.Obj("name" -> "", "msg" -> "Test Message!"))
+      assert(
+        ujson.read(response3.text()) ==
+        ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
+      )
+    }
+    test("javascript") - withServer(MinimalApplication) { host =>
+      val response1 = requests.get(host + "/static/app.js")
+      assert(response1.text().contains("function submitForm()"))
     }
   }
 }
```
## Downstream Examples

- [14.4 - Websockets](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.4%20-%20Websockets)