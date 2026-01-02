def main() =
  pprint.log(
    os.walk(os.pwd)
      .filter(os.isFile)
      .map(path => (os.size(path), path))
      .sortBy(-_(0))
      .take(5)
  )
