 def links = Task.Input{ postInfo.map(_(1)) }
+val previews = Task.sequence(postInfo.map(_(0)).map(post(_).preview))
 def index = Task: