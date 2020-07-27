@ val jsonString = os.read(os.pwd / "ammonite-releases.json")
jsonString: String = """[
  {
    "url": "https://api.github.com/repos/.../releases/17991367",
    "assets_url": "https://api.github.com/repos/.../releases/17991367/assets",
    "upload_url": "https://uploads.github.com/repos/.../releases/17991367/assets",
...

@ val data = ujson.read(jsonString)
data: ujson.Value = Arr(
  ArrayBuffer(
    Obj(
      LinkedHashMap(
        "url" -> Str("https://api.github.com/repos/.../releases/17991367"),
        "assets_url" -> Str("https://api.github.com/repos/.../releases/17991367/assets"),
...
