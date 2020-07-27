$ amm --class-based
Loading...
Welcome to the Ammonite Repl 2.2.0 (Scala 2.13.2 Java 11.0.7)
@ import scala.concurrent._, duration.Duration.Inf, java.util.concurrent.Executors

@ implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
