@ val str = upickle.default.write(os.pwd)
str: String = "\"/Users/lihaoyi/test\""

@ upickle.default.read[os.Path](str)
res39: os.Path = /Users/lihaoyi/test

@ val str2 = upickle.default.write(Array(os.pwd, os.home, os.root))
str2: String = "[\"/Users/lihaoyi/test\",\"/Users/lihaoyi\",\"/\"]"

@ upickle.default.read[Array[os.Path]](str2)
res41: Array[os.Path] = Array(/Users/lihaoyi/test, /Users/lihaoyi, /)
