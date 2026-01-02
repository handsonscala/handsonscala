abstract class SimpleActor[T]()(using cc: Context) extends Actor[T]:
  def run(msg: T): Unit
