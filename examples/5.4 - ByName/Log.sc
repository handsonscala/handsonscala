
var logLevel = 1

def log(level: Int, msg: => String) = {
  if (level > logLevel) println(msg)
}

log(2, "Hello " + 123 + " World")

logLevel = 3
log(2, "Hello " + 123 + " World")
