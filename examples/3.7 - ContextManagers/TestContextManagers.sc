withFileWriter("File.txt") { writer =>
  writer.write("Hello\n"); writer.write("World!")
}
val result = withFileReader("File.txt") { reader =>
  reader.readLine() + "\n" + reader.readLine()
}
assert(result == "Hello\nWorld!")
