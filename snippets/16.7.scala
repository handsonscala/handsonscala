//| mvnDeps:
//| - com.lihaoyi::castor:0.3.0
class SimpleUploadActor()(using cc: castor.Context) extends castor.SimpleActor[String]:
  var count = 0

  def run(msg: String) =
    println(s"Uploading $msg")
    val res = requests.post("https://httpbin.org/post", data=msg)
    count += 1
    println(s"response $count ${res.statusCode} " + ujson.read(res)("data"))

given cc: castor.Context.Test()
val uploader = SimpleUploadActor()