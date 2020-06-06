abstract class SimpleActor[T]()(implicit cc: Context) extends Actor[T]{
  def run(msg: T): Unit
}
