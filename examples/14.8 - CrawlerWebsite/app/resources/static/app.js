function submitForm() {
  var socket = new WebSocket("ws://" + location.host + "/subscribe");
  resultDiv.innerHTML = "";
  socket.onopen = function (ev) {
    socket.send(depthInput.value + " " + searchInput.value);
  };
  socket.onmessage = function (ev) {
    var wrapper = document.createElement('div');
    wrapper.innerHTML = ev.data;
    resultDiv.appendChild(wrapper);
  };
}

