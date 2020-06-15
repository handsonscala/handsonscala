@ println(ujson.write(small))
[{"hello":"world","answer":42},true]

@ os.write(os.pwd / "out.json", small)

@ os.read(os.pwd / "out.json")
res6: String = "[{\"hello\":\"world\",\"answer\":42},true]"
