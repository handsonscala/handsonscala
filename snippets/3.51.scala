@ def myLoop(start: Int, end: Int)
            (callback: Int => Unit) = {
    for (i <- Range(start, end)) {
      callback(i)
    }
  }
