$ curl -X POST --data hello http://localhost:8080/do-thing
olleh

$ ./mill app.test
[50/56] app.test.compile
[info] Compiling 1 Scala source to...
[info] Done compiling.
[56/56] app.test.test
-------------------------------- Running Tests --------------------------------
+ app.ExampleTests.MinimalApplication 629ms
