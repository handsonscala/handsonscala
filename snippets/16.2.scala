trait Actor[T]{
  def send(t: T): Unit

  def sendAsync(f: scala.concurrent.Future[T]): Unit
}
