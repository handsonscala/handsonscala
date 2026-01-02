//| mvnDeps:
//| - com.lihaoyi::castor:0.3.0
class BatchUploadActor()(using cc: castor.Context) extends castor.BatchActor[String]:
  var responseCount = 0

  def runBatch(msgs: Seq[String]) =
    println("runBatch")
    val res = requests.post("https://httpbin.org/post", data = msgs.mkString)
    responseCount += 1
    println(s"response ${res.statusCode} " + ujson.read(res)("data"))

given cc: castor.Context.Test()
val batchUploader = BatchUploadActor()
