local f = function(x) {
  "nested key": "Hello " + x
};
{"key": "value", "thing": f("World")}