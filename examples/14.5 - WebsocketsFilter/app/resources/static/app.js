function submitForm() {
  fetch(
    "/",
    {method: "POST", body: JSON.stringify({name: nameInput.value, msg: msgInput.value})}
  ).then(response => response.json())
   .then(json => {
    if (json["success"]) msgInput.value = ""
    errorDiv.innerText = json["err"]
  })
}

function subscribeSocket(filterString) {
  var socket = new WebSocket("ws://" + location.host + "/subscribe");
  socket.onopen = function (ev) { socket.send(filterString) };
  socket.onmessage = function (ev) { messageList.innerHTML = ev.data };
}