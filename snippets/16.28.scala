val executor = java.util.concurrent.Executors.newSingleThreadExecutor()
val executionContext = scala.concurrent.ExecutionContext.fromExecutor(executor)
implicit val cc = new castor.Context.Test(executionContext)
