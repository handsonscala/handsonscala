import $ivy.`com.lihaoyi::castor:0.1.3`
class DiskActor(logPath: os.Path, rotateSize: Int = 50)
               (implicit cc: castor.Context) extends castor.SimpleActor[String]{
  val oldPath = logPath / os.up / (logPath.last + "-old")
  def run(s: String) = {
    val newLogSize = logSize + s.length + 1
    if (newLogSize <= rotateSize) logSize = newLogSize
    else { // rotate log file by moving it to old path and starting again from empty
      logSize = s.length + 1
      os.move(logPath, oldPath, replaceExisting = true)
    }
    os.write.append(logPath, s + "\n", createFolders = true)
  }
  private var logSize = 0
}