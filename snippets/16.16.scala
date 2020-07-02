import $file.LoggingPipeline, LoggingPipeline.{logger, cc}

logger.send("I am cow")
logger.send("hear me moo")
logger.send("I weight twice as much as you")
logger.send("And I look good on the barbecue")
logger.send("Yoghurt curds cream cheese and butter")
logger.send("Comes from liquids from my udder")
logger.send("I am cow, I am cow")
logger.send("Hear me moo, moooo")

// Logger hasn't finished yet, running in the background
cc.waitForInactivity()
// Now logger has finished

assert(os.read.lines(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
assert(
  os.read.lines(os.pwd / "log.txt") ==
  Seq("I am cow, I am cow", "Hear me moo, moooo")
)