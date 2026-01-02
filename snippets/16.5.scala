abstract class BatchActor[T]()(using cc: Context) extends Actor[T]:
  def runBatch(msgs: Seq[T]): Unit
