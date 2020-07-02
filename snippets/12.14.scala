@ val comments =
    fetchPaginated("https://api.github.com/repos/lihaoyi/upickle/issues/comments")

@ println(comments(0).render(indent = 4))
{
    "url": "https://api.github.com/repos/lihaoyi/upickle/issues/comments/46443901",
    "html_url": "https://github.com/lihaoyi/upickle/issues/1#issuecomment-46443901",
    "issue_url": "https://api.github.com/repos/lihaoyi/upickle/issues/1",
    "id": 46443901,
    "user": { "login": "lihaoyi", ... },
    "created_at": "2014-06-18T14:38:49Z",
    "updated_at": "2014-06-18T14:38:49Z",
    "author_association": "OWNER",
    "body": "Oops, fixed it in trunk, so it'll be fixed next time I publish\n"
}
