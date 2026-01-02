$ mkdir -p bar/src bar/resources

$ echo "Hello" > bar/src/hello.txt; echo "World" > bar/src/world.txt

$ ./mill show bar.zipped
"ref:efdf1f3c:/Users/lihaoyi/test/out/bar/zipped.dest/out.zip"

$ unzip out/bar/zipped.dest/out.zip
Archive:  out/bar/zipped.dest/out.zip
 extracting: concat.txt

$ cat concat.txt
Hello
World
