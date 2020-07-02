import $ivy.`at.favre.lib:bcrypt:0.9.0`
import at.favre.lib.crypto.bcrypt.{BCrypt, LongPasswordStrategies}
import scala.concurrent._, ExecutionContext.Implicits.global, duration.Duration.Inf

val hasher = BCrypt.`with`(LongPasswordStrategies.hashSha512(BCrypt.Version.VERSION_2A))
def hash(name: String) = {
  val salt = name.take(16).padTo(16, ' ').getBytes
  val bytes = hasher.hash(17, salt, os.read.bytes(os.pwd / "files" / name))
  new String(bytes)
}

// Sequential
val (hashes1, duration1) = time{
  for (p <- os.list(os.pwd / "files")) yield {
    println(p)
    hash(p.last)
  }
}
assert(
  pprint.log(hashes1) ==
  Seq(
    "$2a$17$O0fnZkDyZ1bsJknuXw.eG.9Mesh9W03ZnVPefgcTVP7sc2rYBdPb2",
    "$2a$17$Q1Hja0bjJknuXw.eGA.eG.KQ7bQl8kQbzR4sTBdT97icinfz5xh66",
    "$2a$17$RUTrZ1HnWUusYl/lGA.eG.TK2UsZfBYw6mLhDyORr659FFz2lPwZK",
    "$2a$17$UiLjZlPjag3oaEaeGA.eG.8KLuk3HS0iqPGaRJPdp1Bjl4zjhQLWi"
  )
)
pprint.log(duration1)

// Parallel
val (hashes2, duration2) = time{
  val futures = for (p <- os.list(os.pwd / "files")) yield Future{
    println(p)
    hash(p.last)
  }
  futures.map(Await.result(_, Inf))
}

assert(
  pprint.log(hashes2) ==
  Seq(
    "$2a$17$O0fnZkDyZ1bsJknuXw.eG.9Mesh9W03ZnVPefgcTVP7sc2rYBdPb2",
    "$2a$17$Q1Hja0bjJknuXw.eGA.eG.KQ7bQl8kQbzR4sTBdT97icinfz5xh66",
    "$2a$17$RUTrZ1HnWUusYl/lGA.eG.TK2UsZfBYw6mLhDyORr659FFz2lPwZK",
    "$2a$17$UiLjZlPjag3oaEaeGA.eG.8KLuk3HS0iqPGaRJPdp1Bjl4zjhQLWi"
  )
)
pprint.log(duration2)
