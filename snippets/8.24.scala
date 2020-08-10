@ myPrintJson(Asset(1, "hello"))
{"id":1,"name":"hello"}

@ myPrintJson(Seq(1, 2, 3))
[1,2,3]

@ myPrintJson(Seq(Asset(1, "hello"), Asset(2, "goodbye")))
[{"id":1,"name":"hello"},{"id":2,"name":"goodbye"}]
