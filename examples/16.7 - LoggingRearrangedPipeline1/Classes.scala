//| mvnDeps:
//| - com.lihaoyi::castor:0.3.0
class DiskActor(logPath: os.Path, rotateSize: Int = 50)
               (using cc: castor.Context) extends castor.SimpleActor[String]:
  val oldPath = logPath / os.up / (logPath.last + "-old")
  
  def run(s: String) =
    val newLogSize = logSize + s.length + 1
  
    if newLogSize <= rotateSize then logSize = newLogSize
    else // rotate log file by moving it to old path and starting again from empty
      logSize = s.length + 1
      os.move(logPath, oldPath, replaceExisting = true)
  
    os.write.append(logPath, s + "\n", createFolders = true)
  
  private var logSize = 0

class Base64Actor(dest: castor.Actor[String])
                 (using cc: castor.Context) extends castor.SimpleActor[String]:
  def run(msg: String) =
    dest.send(java.util.Base64.getEncoder.encodeToString(msg.getBytes))

class UploadActor(url: String)
                 (using cc: castor.Context) extends castor.SimpleActor[String]:
  def run(msg: String) =
    val res = requests.post(url, data = msg)
    println(s"response ${res.statusCode} " + ujson.read(res)("data"))

class SanitizeActor(dest: castor.Actor[String])
                   (using cc: castor.Context) extends castor.SimpleActor[String]:
  def run(msg: String) =
    dest.send(msg.replaceAll("([0-9]{4})[0-9]{8}([0-9]{4})", "<redacted>"))
