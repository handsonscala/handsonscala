import mill.*
import mill.api.BuildCtx

val items = BuildCtx.watchValue { os.list(BuildCtx.workspaceRoot / "foo").map(_.last) }

object foo extends Cross[FooModule](items)
trait FooModule extends Cross.Module[String]:
  def moduleDir = super.moduleDir / crossValue
  def srcs = Task.Source("src")

  def concat = Task:
    os.write(Task.dest / "concat.txt",  os.list(srcs().path).map(os.read(_)))
    PathRef(Task.dest / "concat.txt")