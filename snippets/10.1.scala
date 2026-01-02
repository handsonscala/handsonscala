import mill.*

def srcs = Task.Source("src")

def concat = Task:
  os.write(Task.dest / "concat.txt",  os.list(srcs().path).map(os.read(_)))
  PathRef(Task.dest / "concat.txt")
