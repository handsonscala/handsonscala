> import scala.concurrent.*, duration.Duration.Inf, java.util.concurrent.Executors

> given ec: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
