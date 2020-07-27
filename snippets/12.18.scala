@ for ((issueId, user, body) <- commentData; newIssueId <- issueNumMap.get(issueId)) {
    println(s"Commenting on issue old_id=$issueId new_id=$newIssueId")
    val resp = requests.post(
      s"https://api.github.com/repos/lihaoyi/test/issues/$newIssueId/comments",
      data = ujson.Obj("body" -> s"$body\nOriginal Author:$user"),
      headers = Map("Authorization" -> s"token $token")
    )
  }
Commenting on issue old_id=1 new_id=1
Commenting on issue old_id=2 new_id=2
...
Commenting on issue old_id=272 new_id=194
