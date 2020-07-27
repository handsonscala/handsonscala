+ def prefixesMatchingString(s: String): Set[String] = {
+   prefixesMatchingString0(s).map(s.substring(0, _))
+ }