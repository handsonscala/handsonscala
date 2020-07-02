@ os.walk(os.pwd).filter(os.isFile).map(p => (os.size(p), p)).sortBy(-_._1).take(5)
res40: IndexedSeq[(Long, os.Path)] = ArraySeq(
  (6340270L, /Users/lihaoyi/test/post/Reimagining/GithubHistory.gif),
  (6008395L, /Users/lihaoyi/test/post/SmartNation/routes.json),
  (5499949L, /Users/lihaoyi/test/post/slides/Why-You-Might-Like-Scala.js.pdf),
  (5461595L, /Users/lihaoyi/test/post/slides/Cross-Platform-Development-in-Scala.js.pdf),
  (4576936L, /Users/lihaoyi/test/post/Reimagining/FluentSearch.gif)
)
