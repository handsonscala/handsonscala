# Example 10.5 - CrossModules
Using cross-modules to dynamically construct a build graph based on the
filesystem

```bash
./mill -i foo[files].concat
./mill -i foo[images].concat
./mill -i foo[text].concat
```
