abstract class BatchActor[T]()(implicit cc: Context) extends Actor[T]{
  def runBatch(msgs: Seq[T]): Unit
}
