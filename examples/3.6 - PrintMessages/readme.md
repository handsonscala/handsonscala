# Example 3.6 - PrintMessages
A method to render a flat array of `Msg` instances into a "threaded"
conversation with child messages printed nested under their parents

```bash
./mill -i TestPrintMessages.scala
./mill -i TestPrintMessages.scala > output.txt
diff expected.txt output.txt
```
