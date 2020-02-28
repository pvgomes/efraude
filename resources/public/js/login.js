function createItem(id, data) {
  localStorage.clear();
  localStorage.setItem(id, JSON.stringify(data));
  // sessionStorage.setItem('user', JSON.stringify(data));
}

function readValue(id) {
  return localStorage.getItem(id);
  //return sessionStorage.getItem(id);
}

function onSignIn(googleUser) {
  var profile = googleUser.getBasicProfile();
    axios({
      method: 'post',
      url: '/social/google',
      headers: {
      "X-CSRF-Token": document.getElementById('__anti-forgery-token').value
      },
      data: {
         id: googleUser.getAuthResponse().id_token,
         name: profile.getName(),
         image: profile.getImageUrl(),
         email: profile.getEmail()
      }
    })
    .then(function (response) {
        createItem("pvguser", response.data);
        window.location.replace("/perfil");
    })
    .catch(function (error) {
      console.log(error);
      alert("Aconteceu um erro, tente mais tarde");
    });
}