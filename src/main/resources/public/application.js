function startApplication() {
    if (!window.fetch) {
        alert("Sorry mate but your browser does not support fetching data from the backend. "
            + "Please use modern browser like Chrome/Chromium!");
        return;
    }

    loadEquipment();
}

function toggleOverlay() {
    var overlay = document.getElementById("overlay");
    overlay.style.display = overlay.style.display === "block" ? "none" : "block";
}

function loadEquipment() {
    var itemsToShow = document.getElementById("itemsToShow");

    toggleOverlay();

    fetch('/api/equipment?limit=' + itemsToShow.value)
        .then(response => response.json())
        .then(data => {
            var container = document.getElementById("equipments");

            while (container.firstChild) {
              container.removeChild(container.firstChild);
            }

            if (data.data) {
                data.data.forEach(item => {
                    var row = document.createElement("tr");

                    var idCol = document.createElement("td");
                    idCol.innerText = item.id;
                    row.appendChild(idCol);

                    var addressCol = document.createElement("td");
                    addressCol.innerText = item.address;
                    row.appendChild(addressCol);

                    var contractStartCol = document.createElement("td");
                    contractStartCol.innerText = new Date(item.contractStartTime).toUTCString()
                    row.appendChild(contractStartCol);

                    var contractEndCol = document.createElement("td");
                    contractEndCol.innerText = new Date(item.contractEndTime).toUTCString()
                    row.appendChild(contractEndCol);

                    var statusCol = document.createElement("td");
                    statusCol.innerText = item.status
                    row.appendChild(statusCol);

                    container.appendChild(row);
                });
            }
        })
        .then(() => toggleOverlay());
}

function itemsToShowChanged() {
    loadEquipment();
}

function clearDb() {
    toggleOverlay();

    if (confirm("Are you sure that you want to clear the DB?")) {
        fetch("/api/equipment", { method: "DELETE" })
            .then(() => loadEquipment())
            .then(() => toggleOverlay());
    }
}

function generateTestData() {
    toggleOverlay();

    fetch("/api/equipment/generate", { method: "POST" })
        .then(() => loadEquipment())
        .then(() => toggleOverlay());
}

function addItem() {
    var formContainer = document.getElementById("equipmentFormContainer");
    formContainer.style.display = "block";
}

function parseDate(value) {
    if (!value) {
        return value;
    }

    return new Date(value).toISOString();
}

function saveItem() {
    toggleOverlay();

    var formContainer = document.getElementById("equipmentFormContainer");
    var form = document.getElementById("equipmentForm");
    var errorRow = document.getElementById("error");

    var values = {
        address: document.getElementById("address").value,
        contractStartTime: parseDate(document.getElementById("contractStartTime").value),
        contractEndTime: parseDate(document.getElementById("contractEndTime").value),
        status: document.getElementById("status").value
    };

    fetch("/api/equipment", {
            method: "POST",
            body: JSON.stringify(values),
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(response => {
            if (!response.ok) {
                var errorMsg = document.getElementById("errorMessage");

                errorRow.removeAttribute("hidden");

                response.json().then(json => {
                    if (json._embedded && json._embedded.errors) {
                        var errors = "";
                        json._embedded.errors.forEach(error => errors += error.message + " ");
                        errorMsg.innerText = errors;
                    } else if (json.message) {
                        errorMsg.innerText = json.message;
                    } else {
                        errorMsg.innerText = response.statusText;
                    }
                });
            } else {
                form.reset();
                errorRow.setAttribute("hidden", "");
                formContainer.style.display = "none";
            }
        })
        .then(() => loadEquipment())
        .then(() => toggleOverlay());

    return false;
}