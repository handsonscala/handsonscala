$ ./mill show sync.assembly
"ref:d5e08f13:/Users/lihaoyi/test/out/sync/assembly/dest/out.jar"

$ find sync -type f
sync/src/Sync.scala

$ mkdir test

$ out/sync/assembly/dest/out.jar sync test

$ find test -type f
test/src/Sync.scala
