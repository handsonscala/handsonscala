# Example 14.8 - CrawlerWebsite
Website that performs parallel crawls of the Wikipedia article graph on behalf
of the user.

```bash
./mill -i app.test
```

## Upstream Example: [14.4 - Websockets](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.4%20-%20Websockets):
Diff:
```diff
diff --git a/14.4 - Websockets/app/resources/static/app.js b/14.8 - CrawlerWebsite/app/resources/static/app.js
index efb34ca..066e267 100644
--- a/14.4 - Websockets/app/resources/static/app.js	
+++ b/14.8 - CrawlerWebsite/app/resources/static/app.js	
@@ -1,13 +1,13 @@
 function submitForm() {
-  fetch(
-    "/",
-    {method: "POST", body: JSON.stringify({name: nameInput.value, msg: msgInput.value})}
-  ).then(response => response.json())
-   .then(json => {
-    if (json["success"]) msgInput.value = ""
-    errorDiv.innerText = json["err"]
-  })
+  var socket = new WebSocket("ws://" + location.host + "/subscribe");
+  resultDiv.innerHTML = "";
+  socket.onopen = function (ev) {
+    socket.send(depthInput.value + " " + searchInput.value);
+  };
+  socket.onmessage = function (ev) {
+    var wrapper = document.createElement('div');
+    wrapper.innerHTML = ev.data;
+    resultDiv.appendChild(wrapper);
+  };
 }
 
-var socket = new WebSocket("ws://" + location.host + "/subscribe");
-socket.onmessage = function (ev) { messageList.innerHTML = ev.data }
\ No newline at end of file
diff --git a/14.4 - Websockets/app/src/MinimalApplication.scala b/14.8 - CrawlerWebsite/app/src/MinimalApplication.scala
index d5614ab..cd43b35 100644
--- a/14.4 - Websockets/app/src/MinimalApplication.scala	
+++ b/14.8 - CrawlerWebsite/app/src/MinimalApplication.scala	
@@ -1,8 +1,7 @@
 package app
 import scalatags.Text.all._
+import scala.concurrent._, duration.Duration.Inf
 object MinimalApplication extends cask.MainRoutes {
-  var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
-  var openConnections = Set.empty[cask.WsChannelActor]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
 
   @cask.staticResources("/static")
@@ -17,37 +16,75 @@ object MinimalApplication extends cask.MainRoutes {
       ),
       body(
         div(cls := "container")(
-          h1("Scala Chat!"),
-          div(id := "messageList")(messageList()),
-          div(id := "errorDiv", color.red),
+          h1("Scala Crawler!"),
           form(onsubmit := "submitForm(); return false")(
-            input(`type` := "text", id := "nameInput", placeholder := "User name"),
-            input(`type` := "text", id := "msgInput", placeholder := "Write a message!"),
+            input(`type` := "text", id := "searchInput", placeholder := "Enter a Wikipedia title!"),
+            input(`type` := "text", id := "depthInput", placeholder := "Enter a search depth"),
             input(`type` := "submit")
-          )
+          ),
+          div(id := "resultDiv"),
+          div(id := "errorDiv", color.red),
         )
       )
     )
   )
 
-  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))
+  def fetchLinks(title: String): Seq[String] = {
+    val resp = requests.get(
+      "https://en.wikipedia.org/w/api.php",
+      params = Seq(
+        "action" -> "query",
+        "titles" -> title,
+        "prop" -> "links",
+        "format" -> "json"
+      )
+    )
+    for{
+      page <- ujson.read(resp)("query")("pages").obj.values.toSeq
+      links <- page.obj.get("links").toSeq
+      link <- links.arr
+    } yield link("title").str
+  }
 
-  @cask.postJson("/")
-  def postChatMsg(name: String, msg: String) = {
-    if (name == "") ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
-    else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
-    else {
-      messages = messages :+ (name -> msg)
-      for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
-      ujson.Obj("success" -> true, "err" -> "")
+  def fetchAllLinksParallel(startTitle: String,
+                            depth: Int,
+                            onResults: Set[String] => Unit,
+                            onDepth: Int => Unit): Set[String] = {
+    var seen = Set(startTitle)
+    var current = Set(startTitle)
+    for (i <- Range(0, depth)) {
+      onDepth(i)
+      val futures = for (title <- current) yield Future{ fetchLinks(title) }
+      val nextTitleLists = futures.map(Await.result(_, Inf))
+      current = nextTitleLists.flatten.filter(!seen.contains(_))
+      onResults(current)
+      seen = seen ++ current
     }
+    seen
   }
 
   @cask.websocket("/subscribe")
   def subscribe() = cask.WsHandler { connection =>
-    connection.send(cask.Ws.Text(messageList().render))
-    openConnections += connection
-    cask.WsActor { case cask.Ws.Close(_, _) => openConnections -= connection }
+    cask.WsActor {
+      case cask.Ws.Text(s"$depth0 $searchTerm") =>
+        fetchAllLinksParallel(
+          searchTerm,
+          depth0.toInt,
+          onResults = results => {
+            connection.send(
+              cask.Ws.Text(
+                div(
+                  results
+                    .toSeq
+                    .flatMap(s => Seq[Frag](" - ", a(href := s"https://en.wikipedia.org/wiki/$s")(s)))
+                    .drop(1)
+                ).render
+              )
+            )
+          },
+          onDepth = i => connection.send(cask.Ws.Text(div(b("Depth ", i + 1)).render))
+        )
+    }
   }
 
   initialize()
diff --git a/14.4 - Websockets/app/test/src/ExampleTests.scala b/14.8 - CrawlerWebsite/app/test/src/ExampleTests.scala
index f0328d2..dd79e77 100644
--- a/14.4 - Websockets/app/test/src/ExampleTests.scala	
+++ b/14.8 - CrawlerWebsite/app/test/src/ExampleTests.scala	
@@ -7,82 +7,40 @@ import castor.Context.Simple.global, cask.util.Logger.Console._
 object ExampleTests extends TestSuite {
   def withServer[T](example: cask.main.Main)(f: String => T): T = {
     val server = io.undertow.Undertow.builder
-      .addHttpListener(8084, "localhost")
+      .addHttpListener(8085, "localhost")
       .setHandler(example.defaultHandler)
       .build
     server.start()
     val res =
-      try f("http://localhost:8084")
+      try f("http://localhost:8085")
       finally server.stop()
     res
   }
 
   val tests = Tests {
     test("success") - withServer(MinimalApplication) { host =>
-      var wsPromise = scala.concurrent.Promise[String]
-      val wsClient = cask.util.WsClient.connect(s"$host/subscribe") {
-        case cask.Ws.Text(msg) => wsPromise.success(msg)
-      }
-      val success = requests.get(host)
-
-      assert(success.text().contains("Scala Chat!"))
-      assert(success.text().contains("alice"))
-      assert(success.text().contains("Hello World!"))
-      assert(success.text().contains("bob"))
-      assert(success.text().contains("I am cow, hear me moo"))
-      assert(success.statusCode == 200)
-
-      val wsMsg = Await.result(wsPromise.future, Inf)
 
-      assert(wsMsg.contains("alice"))
-      assert(wsMsg.contains("Hello World!"))
-      assert(wsMsg.contains("bob"))
-      assert(wsMsg.contains("I am cow, hear me moo"))
+      val success = requests.get(host)
+      assert(success.text().contains("Scala Crawler!"))
 
-      wsPromise = scala.concurrent.Promise[String]
-      val response = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> "Test Message!"))
+      val output = collection.mutable.ArrayDeque.empty[String]
+      val wsClient = cask.util.WsClient.connect(s"$host/subscribe") {
+        case cask.Ws.Text(msg) => output.append(msg)
+      }
+      wsClient.send(cask.Ws.Text("3 Singapore"))
 
-      val parsed = ujson.read(response)
-      assert(parsed("success") == ujson.True)
-      assert(parsed("err") == ujson.Str(""))
+      Thread.sleep(10000)
+      val collection.mutable.ArrayDeque(depth1, res1, depth2, res2, depth3, res3) = output
 
-      assert(response.statusCode == 200)
-      val wsMsg2 = Await.result(wsPromise.future, Inf)
-      assert(wsMsg2.contains("alice"))
-      assert(wsMsg2.contains("Hello World!"))
-      assert(wsMsg2.contains("bob"))
-      assert(wsMsg2.contains("I am cow, hear me moo"))
-      assert(wsMsg2.contains("haoyi"))
-      assert(wsMsg2.contains("Test Message!"))
+      assert(depth1.contains("Depth 1"))
+      assert(res1.contains("+65"))
 
-      val success2 = requests.get(host)
+      assert(depth2.contains("Depth 2"))
+      assert(res2.contains("Telephone numbers in Singapore"))
 
-      assert(success2.text().contains("Scala Chat!"))
-      assert(success2.text().contains("alice"))
-      assert(success2.text().contains("Hello World!"))
-      assert(success2.text().contains("bob"))
-      assert(success2.text().contains("I am cow, hear me moo"))
-      assert(success2.text().contains("haoyi"))
-      assert(success2.text().contains("Test Message!"))
-      assert(success2.statusCode == 200)
-    }
-    test("failure") - withServer(MinimalApplication) { host =>
-      val response1 = requests.post(host, data = ujson.Obj("name" -> "haoyi"), check = false)
-      assert(response1.statusCode == 400)
-      val response2 = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> ""))
-      assert(
-        ujson.read(response2) ==
-        ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
-      )
-      val response3 = requests.post(host, data = ujson.Obj("name" -> "", "msg" -> "Test Message!"))
-      assert(
-        ujson.read(response3) ==
-        ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
-      )
-    }
-    test("javascript") - withServer(MinimalApplication) { host =>
-      val response1 = requests.get(host + "/static/app.js")
-      assert(response1.text().contains("function submitForm()"))
+      assert(depth3.contains("Depth 3"))
+      assert(res3.contains("Ayer Rajah"))
+      println(res1)
     }
   }
 }
diff --git a/14.4 - Websockets/build.sc b/14.8 - CrawlerWebsite/build.sc
index 55bec98..4311bd1 100644
--- a/14.4 - Websockets/build.sc	
+++ b/14.8 - CrawlerWebsite/build.sc	
@@ -4,14 +4,14 @@ object app extends ScalaModule {
   def scalaVersion = "2.13.2"
   def ivyDeps = Agg(
     ivy"com.lihaoyi::scalatags:0.9.1",
-    ivy"com.lihaoyi::cask:0.7.4"
+    ivy"com.lihaoyi::cask:0.7.4",
+    ivy"com.lihaoyi::requests:0.6.5"
   )
   object test extends Tests {
     def testFrameworks = Seq("utest.runner.Framework")
 
     def ivyDeps = Agg(
-      ivy"com.lihaoyi::utest:0.7.4",
-      ivy"com.lihaoyi::requests:0.6.5"
+      ivy"com.lihaoyi::utest:0.7.4"
     )
   }
 }
```
