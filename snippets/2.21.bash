$ ./mill foo.test
120] foo.test.compile compiling 1 Scala source to out/foo/test/compile.dest/classes ...
120] done compiling
127] foo.test.testForked Running Test Class foo.FooTests
127] -------------------------------- Running Tests --------------------------------
127] + foo.FooTests.simple 20ms  <h1>hello</h1>
127] + foo.FooTests.escaping 0ms  <h1>&lt;hello&gt;</h1>
127] Tests: 2, Passed: 2, Failed: 0
127/127] ============================== foo.test ============================== 1s
