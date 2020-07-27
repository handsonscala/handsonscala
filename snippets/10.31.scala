  def links = T.input{ postInfo.map(_._2) }
+ val previews = T.sequence(postInfo.map(_._1).map(post(_).preview))
  def index = T{