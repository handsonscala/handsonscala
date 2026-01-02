def main(srcRepo: String, destRepo: String) =
  val token = os.read(os.home / "github_token.txt").trim

  var secondaryRateLimitHits = 0
  def checkLimit() =
    secondaryRateLimitHits += 1
    if secondaryRateLimitHits % 15 == 0 then
      println("Sleeping for 10 seconds to avoid secondary rate limits...")
      Thread.sleep(10000)

  def fetchPaginated(url: String, params: (String, String)*) =
    var done = false
    var page = 1
    val responses = collection.mutable.Buffer.empty[ujson.Value]

    while !done do
      println("page " + page + "...")
      checkLimit()
      val resp = requests.get(
        url,
        params = Map("page" -> page.toString) ++ params,
        headers = Map("Authorization" -> s"token $token")
      )
      val parsed = ujson.read(resp).arr

      if parsed.length == 0 then done = true
      else responses.appendAll(parsed)

      page += 1

    responses

  val issues =
    fetchPaginated(s"https://api.github.com/repos/$srcRepo/issues", "state" -> "all")

  val nonPullRequests = issues.filter(!_.obj.contains("pull_request"))

  val issueData = for issue <- nonPullRequests yield (
    issue("number").num.toInt,
    issue("title").str,
    issue("body").strOpt.getOrElse(""),
    issue("user")("login").str,
    issue("state").str
  )

  val comments = fetchPaginated(s"https://api.github.com/repos/$srcRepo/issues/comments")

  val commentData = for comment <- comments yield (
    comment("issue_url").str match {
      case s"https://api.github.com/repos/$repo/issues/$id" => id.toInt
    },
    comment("user")("login").str,
    comment("body").str
  )

  val issueNums = for (number, title, body, user, state) <- issueData.sortBy(_(0)) yield
    println(s"Creating issue $number")
    checkLimit()
    val resp = requests.post(
      s"https://api.github.com/repos/$destRepo/issues",
      data = ujson.Obj(
        "title" -> title,
        "body" -> s"$body\nID: $number\nOriginal Author: $user"
      ),
      headers = Map("Authorization" -> s"token $token")
    )
    println(resp.statusCode)
    val newIssueNumber = ujson.read(resp)("number").num.toInt

    if state == "closed" then
      checkLimit()
      requests.patch(
        s"https://api.github.com/repos/$destRepo/issues/$newIssueNumber",
        data = ujson.Obj("state" -> "closed"),
        headers = Map("Authorization" -> s"token $token")
      )

    (number, newIssueNumber)

  val issueNumMap = issueNums.toMap

  for
    (issueId, user, body) <- commentData
    newIssueId <- issueNumMap.get(issueId)
  do
    println(s"Commenting on issue old_id=$issueId new_id=$newIssueId")
    checkLimit()

    val resp = requests.post(
      s"https://api.github.com/repos/$destRepo/issues/$newIssueId/comments",
      data = ujson.Obj("body" -> s"$body\nOriginal Author:$user"),
      headers = Map("Authorization" -> s"token $token")
    )

    println(resp.statusCode)
