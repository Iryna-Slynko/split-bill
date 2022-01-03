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
  let total = 0;
  let totalList = {};
  let people = {};
  receipt.lines.forEach((line, i) => {
    const tableLine = receiptLines[i];
    if (line.claimedByID === null) {
      tableLine.classList.remove('table-warning', 'table-danger');
      const checkbox = tableLine.getElementsByTagName('input')[0];
      checkbox.removeAttribute('disabled');
      checkbox.checked = false;
      tableLine.getElementsByClassName('claimed-by')[0].innerText = '';
    } else {
      const checkbox = tableLine.getElementsByTagName('input')[0];
      checkbox.checked = true;
      tableLine.getElementsByClassName('claimed-by')[0].innerText = line.claimedByName;
      if (line.claimedByID === userId) {
        checkbox.removeAttribute('disabled');
        tableLine.classList.add('table-warning');
        total += line.price;
      } else {

        checkbox.disabled = "disabled";
        tableLine.classList.add('table-danger');
        const total = totalList[line.claimedByID] || 0;
        totalList[line.claimedByID] = total + line.price;
        people[line.claimedByID] = line.claimedByName;
      }
    }
  });
  if (receipt.ownerId === userId) {
    const mainElement = document.createElement("div");
    mainElement.id = "total-data";
    if (total > 0) {
      const element = document.createElement("div");
      element.className = "row";
      element.innerText = "Your total is " + (total/100.0).toFixed(2) + "€";
      mainElement.appendChild(element);
    }

    for (const userid in totalList) {
      if (userid != userId) {
        if (Object.hasOwnProperty.call(totalList, userid)) {
          const userOwes = totalList[userid];
          const userName = people[userid];
          const element = document.createElement("div");
          element.className = "row";
          element.innerText = userName + " owes you " + (userOwes/100.0).toFixed(2) + "€";
          mainElement.appendChild(element);              
        }
      }
    }
    document.getElementById('total-data').replaceWith(mainElement);
  } else {
    if (total > 0) {
      total /= 100.0;
      document.getElementById('total').innerText = "You owe " + total.toFixed(2) + "€";
    } else {
      document.getElementById('total').innerText = "Choose the item!"
    }
  }
  timeoutId = setTimeout(pollGetData, 15000);
}

const pollGetData = async () => {
  const receiptId = receiptTable.dataset.receiptId;
  fetch('/receipt/' + receiptId + '/data?' + new URLSearchParams({
      userId: userId,
      username: username
    }))
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
        body: JSON.stringify({
          'userId': userId,
          'username': username
        })
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