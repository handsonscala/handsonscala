@ var logLevel = 1

@ def log(level: Int, msg: => String) = {
    if (level > logLevel) println(msg)
  }
