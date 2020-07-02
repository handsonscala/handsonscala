import mill._

def srcs = T.source(millSourcePath / "src")

def concat = T{
  os.write(T.dest / "concat.txt",  os.list(srcs().path).map(os.read(_)))
  PathRef(T.dest / "concat.txt")
}