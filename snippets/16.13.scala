//| mvnDeps:
//| - com.lihaoyi::castor:0.3.0
enum Msg:
  case Text(s: String)
  case Flush()

class StateMachineUploadActor(n: Int)(using cc: castor.Context)
extends castor.StateMachineActor[Msg]:
  var responseCount = 0
  def initialState = Idle()

  case class Idle() extends State({
    case Msg.Text(msg) => upload(msg)
  })

  case class Buffering(msgs: Vector[String]) extends State({
    case Msg.Text(s) => Buffering(msgs :+ s)
    case Msg.Flush() => if msgs.isEmpty then Idle() else upload(msgs.mkString)
  })

  def upload(data: String) =
    println("Uploading " + data)
    val res = requests.post("https://httpbin.org/post", data=data)
    responseCount += 1
    println(s"response ${res.statusCode} " + ujson.read(res)("data"))
    cc.scheduleMsg(this, Msg.Flush(), java.time.Duration.ofSeconds(n))
    Buffering(Vector.empty)

given cc: castor.Context.Test()
val stateMachineUploader = StateMachineUploadActor(n = 5)