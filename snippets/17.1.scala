def send[T: upickle.Writer](out: DataOutputStream, msg: T): Unit =
  val bytes = upickle.writeBinary(msg)
  out.writeInt(bytes.length)
  out.write(bytes)
  out.flush()

def receive[T: upickle.Reader](in: DataInputStream) =
  val buf = new Array[Byte](in.readInt())
  in.readFully(buf)
  upickle.readBinary[T](buf)
