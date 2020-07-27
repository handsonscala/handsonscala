import $ivy.`com.lihaoyi::castor:0.1.7`

class BatchUploadActor()(implicit cc: castor.Context)
extends castor.BatchActor[String]{
  var responseCount = 0
  def runBatch(msgs: Seq[String]) = {
    val res = requests.post("https://httpbin.org/post", data = msgs.mkString)
    responseCount += 1
    println(s"response ${res.statusCode} " + ujson.read(res)("data"))
  }
}

implicit val cc = new castor.Context.Test()
val batchUploader = new BatchUploadActor()
