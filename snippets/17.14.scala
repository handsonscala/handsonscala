- def callAgent[T: upickle.Reader](rpc: Rpc): T =
+ def callAgent[T: upickle.Reader](rpc: Rpc): () => T =
    Shared.send(agent.stdin.data, rpc)
-   Shared.receive[T](agent.stdout.data)
+   () => Shared.receive[T](agent.stdout.data)