import $file.FileSync, FileSync._

println("INITIALIZING SRC AND DEST")
os.makeDir(os.pwd / "out")
val src = os.temp.dir(os.pwd / "out")
val dest = os.temp.dir(os.pwd / "out")

os.write(src / "folder1" / "hello.txt", "HELLO", createFolders = true)
os.write(src / "folder1" / "nested" / "world.txt", "world", createFolders = true)

println("FIRST SYNC")
sync(src, dest)

println("FIRST VALIDATION")
assert(os.read(dest / "folder1" / "hello.txt") == "HELLO")
assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "world")

println("UPDATE SRC")
os.write.over(src / "folder1" / "hello.txt", "hello")
os.write.over(src / "folder1" / "nested" / "world.txt", "WORLD")

println("SECOND SYNC")
sync(src, dest)

println("SECOND VALIDATION")
assert(os.read(dest / "folder1" / "hello.txt") == "hello")
assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
