pprint.log(
  os.walk(os.pwd)
    .filter(os.isFile)
    .map(path => (os.size(path), path))
    .sortBy(-_._1)
    .take(5)
)
