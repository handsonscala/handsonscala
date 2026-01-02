//| moduleDeps: [PrintMessages.scala]
def main() =
  printMsgs(Array(
    Msg(0, None, "Hello"),
    Msg(1, Some(0), "World"),
    Msg(2, None, "I am Cow"),
    Msg(3, Some(2), "Hear me moo"),
    Msg(4, Some(2), "I am Cow"),
    Msg(5, Some(4), "Hear me moo, moo")
  ))
