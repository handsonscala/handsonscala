function submitForm() {
  fetch(
    "/",
    {method: "POST", body: JSON.stringify({name: nameInput.value, msg: msgInput.value})}
  ).then(response => response.json())
   .then(json => {
    if (json["success"]) {
      messageList.innerHTML = json["txt"]
      msgInput.value = ""
    }
    errorDiv.innerText = json["err"]
  })
}