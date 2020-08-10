import $file.Arithmetic, Arithmetic._
import fastparse._

assert(
  pprint.log(fastparse.parse("three times seven", parser(_))) ==
    Parsed.Success(value = 21, index = 17)
)
assert(
  pprint.log(fastparse.parse("(eight divide two) times (nine minus four)", parser(_))) ==
    Parsed.Success(value = 20, index = 42)
)
assert(
  pprint.log(fastparse.parse("five times ((nine times eight) minus four)", parser(_))) ==
    Parsed.Success(value = 340, index = 42)
)
