# Example 3.6 - PrintMessages
A method to render a flat array of `Msg` instances into a "threaded"
conversation with child messages printed nested under their parents

```bash
amm --predef PrintMessages.sc TestPrintMessages.sc
amm --predef PrintMessages.sc TestPrintMessages.sc > output.txt
diff expected.txt output.txt
```
