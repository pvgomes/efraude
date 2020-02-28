function createItem(id) {
  sessionStorage.setItem(id, uuidv4());
}

function readValue(id) {
  return sessionStorage.getItem(id);
}

function uuidv4() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

function fraudUp(id) {
  if (readValue(id)) {
    alert("você já votou");
    return false;
  }
  axios({
    method: 'post',
    url: '/fraud/up',
    headers: {
    "X-CSRF-Token": document.getElementById('__anti-forgery-token').value
    },
    data: {
       id: id,
       hash: uuidv4()
    }
  })
  .then(function (response) {
      createItem(id);
      alert("obrigado pelo seu voto");
    })
    .catch(function (error) {
      console.log(error);
      alert("Aconteceu um erro no seu voto, tente mais tarde");
    });
}

function fraudDown(id) {
  if (readValue(id)) {
    alert("você já votou");
    return false;
  }

  axios({
    method: 'post',
    url: '/fraud/down',
    headers: {
    "X-CSRF-Token": document.getElementById('__anti-forgery-token').value
    },
    data: {
       id: id,
       hash: uuidv4()
    }
  })
  .then(function (response) {
      createItem(id);
      alert("obrigado pelo seu voto");
  })
  .catch(function (error) {
    console.log(error);
    alert("Aconteceu um erro no seu voto, tente mais tarde. Se você já votou não é possível votar duas vezes.");
  });
}


document.addEventListener('DOMContentLoaded', () => {
  (document.querySelectorAll('.notification .delete') || []).forEach(($delete) => {
    $notification = $delete.parentNode;

    $delete.addEventListener('click', () => {
      $notification.parentNode.removeChild($notification);
    });
  });
});