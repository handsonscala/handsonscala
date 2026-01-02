-val postInfo = os
- .list(os.pwd / "post")
- .map: p =>
-   val s"$prefix - $suffix.md" = p.last
-   (prefix, suffix, p)
- .sortBy(_(0).toInt)
+import mill.api.BuildCtx
+val postInfo = BuildCtx.watchValue:
+ os.list(BuildCtx.workspaceRoot / "post")
+   .map: p =>
+     val s"$prefix - $suffix.md" = p.last
+     (prefix, suffix, p)
+   .sortBy(_(0).toInt)