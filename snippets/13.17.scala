-       val nextTitleLists = for (title <- current) yield fetchLinks(title)
+       val futures = for (title <- current) yield Future{ fetchLinks(title) }
+       val nextTitleLists = futures.map(Await.result(_, Inf))
