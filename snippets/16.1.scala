class SimpleUploadActor()(implicit cc: castor.Context) extends castor.SimpleActor[String]{
  def run(msg: String) = {
    val res = requests.post("https://httpbin.org/post", data = msg)
    println("response " + res.statusCode)
  }
}
