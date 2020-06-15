@ val (hashes, duration) = time{
    for (p <- os.list(os.pwd)) yield {
      println("Hashing " + p)
      hash(p.last)
    }
  }
Hashing /Users/lihaoyi/test/Chinatown.jpg
Hashing /Users/lihaoyi/test/Kresge.jpg
Hashing /Users/lihaoyi/test/Memorial.jpg
Hashing /Users/lihaoyi/test/ZCenter.jpg
hashes: IndexedSeq[String] = ArraySeq(
  "$2a$17$O0fnZkDyZ1bsJknuXw.eG.9Mesh9W03ZnVPefgcTVP7sc2rYBdPb2",
  "$2a$17$Q1Hja0bjJknuXw.eGA.eG.KQ7bQl8kQbzR4sTBdT97icinfz5xh66",
  "$2a$17$RUTrZ1HnWUusYl/lGA.eG.TK2UsZfBYw6mLhDyORr659FFz2lPwZK",
  "$2a$17$UiLjZlPjag3oaEaeGA.eG.8KLuk3HS0iqPGaRJPdp1Bjl4zjhQLWi"
)
duration: FiniteDuration = 38949493436 nanoseconds
