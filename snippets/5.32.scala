> var logLevel = 1

> def log(l: Int, msg: => String) =
    if l > logLevel
    then println(msg)
