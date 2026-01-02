//| moduleDeps: [Blog.scala]

@main def testMain() =
  main()

  assert(pprint.log(os.read(os.pwd / "out/index.html")).contains("Written on 2025-"))
  assert(pprint.log(os.read(os.pwd / "out/post/my-first-post.html")).contains("Written on 2025-"))
  assert(pprint.log(os.read(os.pwd / "out/post/my-second-post.html")).contains("Written on 2025-"))
  assert(pprint.log(os.read(os.pwd / "out/post/my-third-post.html")).contains("Written on 2025-"))
