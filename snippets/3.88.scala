object EmptyInputStream extends java.io.InputStream:
  // return -1 to immediately signal end of stream without returning any data
  def read(): Int = -1
