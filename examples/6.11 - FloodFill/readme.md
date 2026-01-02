# Example 6.11 - FloodFill
Breadth-first implemention of a floodfill algorithm, taking a callback that
allows the user to specify what color pairs are similar enough to traverse

```bash
mv Filled.jpg ExpectedFilled.jpg
./mill -i ExampleApiUsage.scala
./mill -i TestFloodFill.scala
diff Filled.jpg ExpectedFilled.jpg
```
