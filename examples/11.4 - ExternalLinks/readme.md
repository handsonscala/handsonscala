# Example 11.4 - ExternalLinks
Crawling the pages of a static website to extract every external link on the
site

```bash
amm ExternalLinks.sc > output.txt
grep http://www.lihaoyi.com/fastparse/ output.txt
grep https://github.com/jparsec/jparsec output.txt
grep http://ammonite.io output.txt
grep http://pyjs.org/ output.txt
```
