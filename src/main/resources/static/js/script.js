//@ts-check"
'use strict';
const modal = document.getElementById('name-modal');
const myModal = new bootstrap.Modal(modal);

function enableEditing() {
  if ((username !== "") && (username !== null)) {
    document.querySelectorAll('input').forEach(function (element) {
      element.removeAttribute('disabled');
    });
    const userIdInput = document.getElementById('userid');
    if (userIdInput !== null) {
      userIdInput.value = userId;
      document.getElementById('username').value = username;
    }
    document.getElementById('user-header').innerText = "Welcome, " + username;
  } else {
    myModal.show();
  }
}

function makePutRequest(url) {
  fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({'userid': userId, 'username': username})
   }).then(response => response.json())
  .then(receipt => {
    console.log(receipt);
  })
}

let username = localStorage.getItem('username');
let userId = localStorage.getItem('userid') || crypto.randomUUID();

modal.addEventListener('hidden.bs.modal', function (event) {
  username = document.getElementById('introduction-name').value.trim();
  localStorage.setItem("username", username);
  localStorage.setItem("userid", userId);
  enableEditing();
});
document.getElementById('form-intro').addEventListener('submit', function (event) {
  event.preventDefault();
  myModal.hide();
});

enableEditing();

const receiptTable = document.querySelector("#receipt-table");
if (receiptTable !== null) {
  const receiptId = receiptTable.dataset.receiptId;
  receiptTable.addEventListener("click", function (event) {
    const element = event.target.closest("tr");
    const checkbox = element.getElementsByTagName("input")[0];
    if (checkbox === event.target) {
      return;
    }
    if (checkbox.disabled) {
      return;
    }
    const rowId = checkbox.dataset.rowId;
    checkbox.checked = !checkbox.checked;
    if (checkbox.checked) {
      makePutRequest('/receipt/'+receiptId+'/claim/'+rowId);
      element.classList.add('table-warning');
    } else {
      makePutRequest('/receipt/'+receiptId+'/unclaim/'+rowId);
      element.classList.remove('table-warning');
    }
  });
  receiptTable.addEventListener("change", function (event) {
    const checkbox = event.target;
    const element = checkbox.closest("tr");
    if (checkbox.checked) {
      element.classList.add('table-warning');
    } else {
      element.classList.remove('table-warning');
    }
  });
}