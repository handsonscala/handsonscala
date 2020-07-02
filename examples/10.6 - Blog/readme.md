# Example 10.6 - Blog
Our static blog generator, converted into an incremental Mill build pipeline

```bash
./mill -i dist
```

## Upstream Example: [9.6 - Deploy](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.6%20-%20Deploy):
Diff:
```diff
diff --git a/9.6 - Deploy/Blog.sc b/9.6 - Deploy/Blog.sc
deleted file mode 100644
index 3bb32bf..0000000
--- a/9.6 - Deploy/Blog.sc	
+++ /dev/null
@@ -1,63 +0,0 @@
-import $ivy.`com.lihaoyi::scalatags:0.9.1`, scalatags.Text.all._
-import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
-
-@main def main(targetGitRepo: String = ""): Unit = {
-  interp.watch(os.pwd / "post")
-  val postInfo = os
-    .list(os.pwd / "post")
-    .map{ p =>
-      val s"$prefix - $suffix.md" = p.last
-      (prefix, suffix, p)
-    }
-    .sortBy(_._1.toInt)
-
-  def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"
-
-  val bootstrapCss = link(
-    rel := "stylesheet",
-    href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
-  )
-
-  os.remove.all(os.pwd / "out")
-  os.makeDir.all(os.pwd / "out" / "post")
-
-  for ((_, suffix, path) <- postInfo) {
-    val parser = org.commonmark.parser.Parser.builder().build()
-    val document = parser.parse(os.read(path))
-    val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
-    val output = renderer.render(document)
-    os.write(
-      os.pwd / "out" / "post" / mdNameToHtml(suffix),
-      doctype("html")(
-        html(
-          head(bootstrapCss),
-          body(
-            h1(a("Blog", href := "../index.html"), " / ", suffix),
-            raw(output)
-          )
-        )
-      )
-    )
-  }
-
-  os.write(
-    os.pwd / "out" / "index.html",
-    doctype("html")(
-      html(
-        head(bootstrapCss),
-        body(
-          h1("Blog"),
-          for ((_, suffix, _) <- postInfo)
-          yield h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix))
-        )
-      )
-    )
-  )
-
-  if (targetGitRepo != "") {
-    os.proc("git", "init").call(cwd = os.pwd / "out")
-    os.proc("git", "add", "-A").call(cwd = os.pwd / "out")
-    os.proc("git", "commit", "-am", ".").call(cwd = os.pwd / "out")
-    os.proc("git", "push", targetGitRepo, "head", "-f").call(cwd = os.pwd / "out")
-  }
-}
diff --git a/9.6 - Deploy/TestBlog.sc b/9.6 - Deploy/TestBlog.sc
deleted file mode 100644
index 4ab6f4b..0000000
--- a/9.6 - Deploy/TestBlog.sc	
+++ /dev/null
@@ -1,18 +0,0 @@
-import $file.Blog, Blog._
-
-assert(
-  pprint.log(os.read(os.pwd / "out" / "index.html"))
-    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-)
-assert(
-  pprint.log(os.read(os.pwd / "out" / "post" / "my-first-post.html"))
-    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-)
-assert(
-  pprint.log(os.read(os.pwd / "out" / "post" / "my-second-post.html"))
-    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-)
-assert(
-  pprint.log(os.read(os.pwd / "out" / "post" / "my-third-post.html"))
-    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-)
diff --git a/10.6 - Blog/build.sc b/10.6 - Blog/build.sc
new file mode 100644
index 0000000..88b6b4d
--- /dev/null
+++ b/10.6 - Blog/build.sc	
@@ -0,0 +1,73 @@
+import $ivy.`com.lihaoyi::scalatags:0.9.1`, scalatags.Text.all._
+import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
+import mill._
+
+def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"
+
+val postInfo = interp.watchValue {
+  os.list(os.pwd / "post")
+    .map { p =>
+      val s"$prefix - $suffix.md" = p.last
+      (prefix, suffix, p)
+    }
+    .sortBy(_._1.toInt)
+}
+
+val bootstrapCss = link(
+  rel := "stylesheet",
+  href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
+)
+
+object post extends Cross[PostModule](postInfo.map(_._1):_*)
+class PostModule(number: String) extends Module{
+  val Some((_, suffix, path)) = postInfo.find(_._1 == number)
+  def srcPath = T.source(path)
+  def render = T{
+    val parser = org.commonmark.parser.Parser.builder().build()
+    val document = parser.parse(os.read(srcPath().path))
+    val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
+    val output = renderer.render(document)
+    os.write(
+      T.dest /  mdNameToHtml(suffix),
+      doctype("html")(
+        html(
+          head(bootstrapCss),
+          body(
+            h1(a("Blog", href := "../index.html"), " / ", suffix),
+            raw(output)
+          )
+        )
+      )
+    )
+    PathRef(T.dest / mdNameToHtml(suffix))
+  }
+}
+
+def links = T.input{ postInfo.map(_._2) }
+
+def index = T{
+  os.write(
+    T.dest / "index.html",
+    doctype("html")(
+      html(
+        head(bootstrapCss),
+        body(
+          h1("Blog"),
+          for (suffix <- links())
+          yield h2(a(suffix, href := ("post/" + mdNameToHtml(suffix))))
+        )
+      )
+    )
+  )
+  PathRef(T.dest / "index.html")
+}
+
+val posts = T.sequence(postInfo.map(_._1).map(post(_).render))
+
+def dist = T {
+  for (post <- posts()) {
+    os.copy(post.path, T.dest / "post" / post.path.last, createFolders = true)
+  }
+  os.copy(index().path, T.dest / "index.html")
+  PathRef(T.dest)
+}
diff --git a/10.6 - Blog/mill b/10.6 - Blog/mill
new file mode 100755
index 0000000..553da30
--- /dev/null
+++ b/10.6 - Blog/mill	
@@ -0,0 +1,37 @@
+#!/usr/bin/env sh
+
+# This is a wrapper script, that automatically download mill from GitHub release pages
+# You can give the required mill version with MILL_VERSION env variable
+# If no version is given, it falls back to the value of DEFAULT_MILL_VERSION
+DEFAULT_MILL_VERSION=0.7.3
+
+set -e
+
+if [ -z "$MILL_VERSION" ] ; then
+  if [ -f ".mill-version" ] ; then
+    MILL_VERSION="$(head -n 1 .mill-version 2> /dev/null)"
+  elif [ -f "mill" ] && [ "$BASH_SOURCE" != "mill" ] ; then
+    MILL_VERSION=$(grep -F "DEFAULT_MILL_VERSION=" "mill" | head -n 1 | cut -d= -f2)
+  else
+    MILL_VERSION=$DEFAULT_MILL_VERSION
+  fi
+fi
+
+MILL_DOWNLOAD_PATH="$HOME/.mill/download"
+MILL_EXEC_PATH="${MILL_DOWNLOAD_PATH}/$MILL_VERSION"
+
+if [ ! -x "$MILL_EXEC_PATH" ] ; then
+  mkdir -p $MILL_DOWNLOAD_PATH
+  DOWNLOAD_FILE=$MILL_EXEC_PATH-tmp-download
+  MILL_DOWNLOAD_URL="https://github.com/lihaoyi/mill/releases/download/${MILL_VERSION%%-*}/$MILL_VERSION-assembly"
+  curl --fail -L -o "$DOWNLOAD_FILE" "$MILL_DOWNLOAD_URL"
+  chmod +x "$DOWNLOAD_FILE"
+  mv "$DOWNLOAD_FILE" "$MILL_EXEC_PATH"
+  unset DOWNLOAD_FILE
+  unset MILL_DOWNLOAD_URL
+fi
+
+unset MILL_DOWNLOAD_PATH
+unset MILL_VERSION
+
+exec $MILL_EXEC_PATH "$@"
```
## Downstream Examples

- [10.7 - ExtendedBlog](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.7%20-%20ExtendedBlog)