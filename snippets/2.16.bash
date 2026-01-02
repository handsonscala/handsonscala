$ export REPO=https://repo1.maven.org/maven2/com/lihaoyi/mill-dist/1.1.0-RC3-122-0af08c

$ export FILENAME=mill-dist-1.1.0-RC3-122-0af08c-example-scalalib-basic-6-programmable

$ curl -L "$REPO/$FILENAME.zip" -o "$FILENAME.zip"

$ unzip "$FILENAME.zip" && rm "$FILENAME.zip"

$ mv mill-dist-1.1.0-RC3-122-0af08c-example-scalalib-basic-6-programmable/* .

$ find . -type f
./foo/src/Foo.scala
./foo/test/src/FooTests.scala
./build.mill
./mill
./mill.bat
