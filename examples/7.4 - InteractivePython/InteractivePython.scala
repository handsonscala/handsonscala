def main() =
  val sub = os.spawn(cmd = ("python3", "-u", "-c", "while True: print(eval(input()))"))

  sub.stdin.write("1 + 2")

  sub.stdin.writeLine("+ 4")

  sub.stdin.flush()

  pprint.log(sub.stdout.readLine())

  sub.stdin.write("'1' + '2'")

  sub.stdin.writeLine("+ '4'")

  sub.stdin.flush()

  sub.stdout.readLine()

  sub.stdin.write("1 * 2".getBytes)

  sub.stdin.write("* 4\n".getBytes)

  sub.stdin.flush()

  val byte = sub.stdout.read()
  pprint.log(byte)
  pprint.log(byte.toChar)

  pprint.log(sub.isAlive())

  sub.destroy()

  Thread.sleep(1000)

  pprint.log(sub.isAlive())
