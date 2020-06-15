# Example 10.3 - Modules
Simple non-linear build pipeline, replicated in several modules

```bash
./mill -i bar.concat
./mill -i bar.compress
./mill -i bar.zipped
./mill -i qux.concat
./mill -i qux.compress
./mill -i qux.zipped
```

## Upstream Example: [10.2 - Nonlinear](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.2%20-%20Nonlinear):
Diff:
```diff
diff --git a/10.3 - Modules/bar/resources/Cambridge Skyline 1.png b/10.3 - Modules/bar/resources/Cambridge Skyline 1.png
new file mode 100644
index 0000000..489eac0
Binary files /dev/null and b/10.3 - Modules/bar/resources/Cambridge Skyline 1.png differ
diff --git a/10.3 - Modules/bar/resources/Cambridge Skyline 2.png b/10.3 - Modules/bar/resources/Cambridge Skyline 2.png
new file mode 100644
index 0000000..463e2a2
Binary files /dev/null and b/10.3 - Modules/bar/resources/Cambridge Skyline 2.png differ
diff --git a/10.3 - Modules/bar/resources/Cambridge Skyline.fw.png b/10.3 - Modules/bar/resources/Cambridge Skyline.fw.png
new file mode 100644
index 0000000..cab4b08
Binary files /dev/null and b/10.3 - Modules/bar/resources/Cambridge Skyline.fw.png differ
diff --git a/10.3 - Modules/bar/src/1 - My First Post.md b/10.3 - Modules/bar/src/1 - My First Post.md
new file mode 100644
index 0000000..f46de00
--- /dev/null
+++ b/10.3 - Modules/bar/src/1 - My First Post.md	
@@ -0,0 +1,5 @@
+Sometimes you want numbered lists:
+
+1. One
+2. Two
+3. Three
\ No newline at end of file
diff --git a/10.2 - Nonlinear/build.sc b/10.3 - Modules/build.sc
index ce4c01f..7a5612a 100644
--- a/10.2 - Nonlinear/build.sc	
+++ b/10.3 - Modules/build.sc	
@@ -1,26 +1,32 @@
 import mill._
 
-def srcs = T.source(millSourcePath / "src")
-def resources = T.source(millSourcePath / "resources")
+trait FooModule extends Module {
+  def srcs = T.source(millSourcePath / "src")
 
-def concat = T{
+  def resources = T.source(millSourcePath / "resources")
+
+  def concat = T {
     os.write(T.dest / "concat.txt", os.list(srcs().path).map(os.read(_)))
     PathRef(T.dest / "concat.txt")
-}
+  }
 
-def compress = T{
+  def compress = T {
     for (p <- os.list(resources().path)) {
       val copied = T.dest / p.relativeTo(resources().path)
       os.copy(p, copied)
       os.proc("gzip", copied).call()
     }
     PathRef(T.dest)
-}
-def zipped = T{
+  }
+
+  def zipped = T {
     val temp = T.dest / "temp"
     os.makeDir(temp)
     os.copy(concat().path, temp / "concat.txt")
     for (p <- os.list(compress().path)) os.copy(p, temp / p.relativeTo(compress().path))
     os.proc("zip", "-r", T.dest / "out.zip", ".").call(cwd = temp)
     PathRef(T.dest / "out.zip")
+  }
 }
+object bar extends FooModule
+object qux extends FooModule
diff --git a/10.3 - Modules/qux/resources/boston skyline 1.png b/10.3 - Modules/qux/resources/boston skyline 1.png
new file mode 100644
index 0000000..22fffcb
Binary files /dev/null and b/10.3 - Modules/qux/resources/boston skyline 1.png differ
diff --git a/10.3 - Modules/qux/resources/boston skyline 2.png b/10.3 - Modules/qux/resources/boston skyline 2.png
new file mode 100644
index 0000000..f0acde2
Binary files /dev/null and b/10.3 - Modules/qux/resources/boston skyline 2.png differ
diff --git a/10.3 - Modules/qux/resources/boston skyline.fw.png b/10.3 - Modules/qux/resources/boston skyline.fw.png
new file mode 100644
index 0000000..57420ba
Binary files /dev/null and b/10.3 - Modules/qux/resources/boston skyline.fw.png differ
diff --git a/10.3 - Modules/qux/src/2 - My Second Post.md b/10.3 - Modules/qux/src/2 - My Second Post.md
new file mode 100644
index 0000000..49fffcb
--- /dev/null
+++ b/10.3 - Modules/qux/src/2 - My Second Post.md	
@@ -0,0 +1,5 @@
+# Structured documents
+
+Sometimes it's useful to have different levels of headings to structure your documents. Start lines with a `#` to create headings. Multiple `##` in a row denote smaller heading sizes.
+
+### This is a third-tier heading
\ No newline at end of file
diff --git a/10.3 - Modules/qux/src/3 - My Third Post.md b/10.3 - Modules/qux/src/3 - My Third Post.md
new file mode 100644
index 0000000..ac859c1
--- /dev/null
+++ b/10.3 - Modules/qux/src/3 - My Third Post.md	
@@ -0,0 +1,5 @@
+There are many different ways to style code with GitHub's markdown. If you have inline code blocks, wrap them in backticks: `var example = true`.  If you've got a longer block of code, you can indent with four spaces:
+
+    if (isAwesome) {
+      return true
+    }
\ No newline at end of file
diff --git a/10.2 - Nonlinear/resources/Design.fw.png b/10.2 - Nonlinear/resources/Design.fw.png
deleted file mode 100644
index 3d781b6..0000000
Binary files a/10.2 - Nonlinear/resources/Design.fw.png and /dev/null differ
diff --git a/10.2 - Nonlinear/resources/MIT-Seal.gif b/10.2 - Nonlinear/resources/MIT-Seal.gif
deleted file mode 100644
index 16e75ef..0000000
Binary files a/10.2 - Nonlinear/resources/MIT-Seal.gif and /dev/null differ
diff --git a/10.2 - Nonlinear/src/hello.txt b/10.2 - Nonlinear/src/hello.txt
deleted file mode 100644
index 95d09f2..0000000
--- a/10.2 - Nonlinear/src/hello.txt	
+++ /dev/null
@@ -1 +0,0 @@
-hello world
\ No newline at end of file
diff --git a/10.2 - Nonlinear/src/iamcow.txt b/10.2 - Nonlinear/src/iamcow.txt
deleted file mode 100644
index fcd2704..0000000
--- a/10.2 - Nonlinear/src/iamcow.txt	
+++ /dev/null
@@ -1 +0,0 @@
-hear me moo
\ No newline at end of file
```
