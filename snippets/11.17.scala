@ articles.length
res15: Int = 917

@ articles.map(_._5.length).sum
res16: Int = 16583

@ os.write.over(os.pwd / "docs.json", upickle.default.write(articles, indent = 4))

@ os.read(os.pwd / "docs.json")
res65: String = """[
    [
        "/en-US/docs/Web/API/ANGLE_instanced_arrays",
        "The ANGLE_instanced_arrays extension is part of the WebGL API and allows...",
...
