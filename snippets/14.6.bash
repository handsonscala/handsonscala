$ curl -X POST --data hello http://localhost:8080/do-thing
olleh

$ ./mill app.test
[116/123] app.test.compile
[116] [info] Compiling 1 Scala source to...
[116] [info] Done compiling.
[123/123] app.test.testForked
-------------------------------- Running Tests --------------------------------
[123] + app.ExampleTests.MinimalApplication 323ms
[123] Tests: 1, Passed: 1, Failed: 0
