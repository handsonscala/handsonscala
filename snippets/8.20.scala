> val str = upickle.write(os.pwd)
str: String = "\"/Users/lihaoyi/test\""

> upickle.read[os.Path](str)
res17: os.Path = /Users/lihaoyi/test

> val str2 = upickle.write(Array(os.pwd, os.home, os.root))
str2: String = "[\"/Users/lihaoyi/test\",\"/Users/lihaoyi\",\"/\"]"

> upickle.read[Array[os.Path]](str2)
res18: Array[os.Path] = Array(
  /Users/lihaoyi/test, /Users/lihaoyi, /
)
