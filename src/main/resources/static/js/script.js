//@ts-check"
'use strict';
const modal = document.getElementById('name-modal');
const myModal = new bootstrap.Modal(modal);
const receiptTable = document.querySelector("#receipt-table");

let username = localStorage.getItem('username');
let userId = localStorage.getItem('userid') || crypto.randomUUID();
let timeoutId = null;

function updateReceiptData(receipt) {
  if (receiptTable === null) {
    return;
  }

  if (timeoutId !== null) {
    clearTimeout(timeoutId);
    timeoutId = null;
  }

  const receiptLines = receiptTable.getElementsByTagName('tr');
  receipt.lines.forEach((line, i) => {
    const tableLine = receiptLines[i];
    if (line.claimedByID === null) {
      tableLine.classList.remove('table-warning', 'table-danger');
      const checkbox = tableLine.getElementsByTagName('input')[0];
      checkbox.removeAttribute('disabled');
      checkbox.checked = false;
    } else {
      const checkbox = tableLine.getElementsByTagName('input')[0];
      checkbox.checked = true;
      if (line.claimedByID === userId) {
        checkbox.removeAttribute('disabled');
        tableLine.classList.add('table-warning');
      } else {
        checkbox.disabled = "disabled";
        tableLine.classList.add('table-danger');
      }
    }
  });
  timeoutId = setTimeout(pollGetData, 15000);
}

const pollGetData = async () => {
  const receiptId = receiptTable.dataset.receiptId;
  fetch('/receipt/' + receiptId + '/data?' + new URLSearchParams({userId: userId, username: username}))
  .then(response => response.json())
  .then(updateReceiptData);
}

if (receiptTable !== null) {
  function makePutRequest(url) {
    fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ 'userId': userId, 'username': username })
    }).then(response => response.json())
      .then(updateReceiptData);
  }

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
    clearTimeout(timeoutId);
    timeoutId = null;
    const rowId = checkbox.dataset.rowId;
    checkbox.checked = !checkbox.checked;
    if (checkbox.checked) {
      makePutRequest('/receipt/' + receiptId + '/claim/' + rowId);
      element.classList.add('table-warning');
    } else {
      makePutRequest('/receipt/' + receiptId + '/unclaim/' + rowId);
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

function enableEditing() {
  if ((username !== "") && (username !== null)) {
    const userIdInput = document.getElementById('userid');
    if (userIdInput !== null) {
      userIdInput.value = userId;
      document.getElementById('username').value = username;
    }
    document.getElementById('user-header').innerText = "Welcome, " + username;

    if (receiptTable !== null) {
      pollGetData();
    }  
  } else {
    myModal.show();
  }
}

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