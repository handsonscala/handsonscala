@ val issueNums = for ((number, title, body, user) <- issueData.sortBy(_._1)) yield {
    println(s"Creating issue $number")
    val resp = requests.post(
      s"https://api.github.com/repos/lihaoyi/test/issues",
      data = ujson.Obj(
        "title" -> title,
        "body" -> s"$body\nID: $number\nOriginal Author: $user"
      ),
      headers = Map("Authorization" -> s"token $token")
    )
    val newIssueNumber = ujson.read(resp.text())("number").num.toInt
    (number, newIssueNumber)
  }
Creating issue 1
Creating issue 2
...
Creating issue 272
