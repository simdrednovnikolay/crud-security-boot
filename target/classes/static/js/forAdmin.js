document.getElementById("editForm").addEventListener("submit", editPost)

function editPost(e){
    e.preventDefault();

    let id = document.getElementById("idEdit").value;
    let name = document.getElementById("nameEdit").value;
    let password = document.getElementById("passwordEdit").value;
    let roles = setRoles(Array.from(document.getElementById("roleEdit").selectedOptions)
        .map(option => option.value));

    fetch("http://localhost:8080/editUser", {
        method:"PUT",
        headers: {
            "Accept": "application/json, text/plain, */*",
            "Content-type":"application/json"
        },
        body:JSON.stringify({
            id:id,
            name:name,
            password:password,
            roles:roles
        })
    }).finally(() => {
        $('#modalEdit').modal("hide")
        allUsers();
    })
}

document.getElementById("deleteForm").addEventListener("submit", deletePost)

function deletePost(e){
    e.preventDefault();

    let id = document.getElementById("idDelete").value;
    let firstName = document.getElementById("nameDelete").value;
    let password = document.getElementById("passwordDelete").value;
    let roles = setRoles(Array.from(document.getElementById("roleDelete").selectedOptions)
        .map(option => option.value));

    fetch("http://localhost:8080/deleteUser/" + id, {
        method:"DELETE",
        headers: {
            "Accept": "application/json, text/plain, */*",
            "Content-type":"application/json"
        },
        body:JSON.stringify({
            id:id,
            name:name,
            password:password,
            roles:roles
        })
    }).finally(() => {
        $('#modalDelete').modal("hide")
        allUsers();
    })
}

document.getElementById("newUserForm").addEventListener("submit", addNewUser);

function addNewUser(e){
    e.preventDefault();

    let firstName = document.getElementById("nameNew").value;
    let password = document.getElementById("passwordNew").value;
    let roles = setRoles(Array.from(document.getElementById("roleNew").selectedOptions)
        .map(option => option.value));

    fetch("http://localhost:8080/addUser", {
        method: "POST",
        headers: {
            "Accept": "application/json, text/plain, */*",
            "Content-type": "application/json"
        },
        body: JSON.stringify({
            name: firstName,
            password: password,
            roles: roles
        })
    })
        .finally(() => {
            document.getElementById("idUsersTable").click();
            allUsers();
            document.getElementById("newUserForm").reset();
        })
}

function setRoles(someRoles) {
    let roles = [];
    if (someRoles.indexOf("ROLE_USER") >= 0) {
        roles.push({"id": 2, "role": "ROLE_USER"});
    }
    if (someRoles.indexOf("ROLE_ADMIN") >= 0) {
        roles.push({"id": 1, "role": "ROLE_ADMIN"});
    }
    return roles;
}
function getRoles() {
    fetch("http://localhost:8080/allRoles").then((res) => res.json())
        .then((data) => {
            let output = "";
            data.forEach(function (role) {
                output += `<option>${role.name}</option>`;
            });
            document.getElementById("roleNew").innerHTML = output;
            document.getElementById("roleEdit").innerHTML = output;
            document.getElementById("roleDelete").innerHTML = output;
        })
}
getRoles()

function allUsers() {
    fetch("http://localhost:8080/allUsers")
        .then((res) => res.json())
        .then((data) => {
            let output = "";
            data.forEach(function (user) {

                output += `
                <tr>
                <td id="id${user.id}">${user.id}</td>
                <td id="name${user.id}">${user.name}</td>
                <td id="password${user.id}">${user.password}</td>
                <td id="roles${user.id}">${user.roles.map(role => role.name)}</td>
                <td>
                <a class="btn btn-info" role="button" data-target="#modalEdit"
                data-toggle="modal" onclick="openModalWindowEdit(${user.id})">Edit</a>
                </td>
                <td>
                <a class="btn btn-danger" role="button" data-target="#modalDelete"
                data-toggle="modal" onclick="openModalWindowDelete(${user.id})">Delete</a>
                </td>
              </tr>
          `;
            });
            document.getElementById("allUsers").innerHTML = output;
        })
}

function openModalWindowEdit(id) {
    document.getElementById("idEdit").value = id;
    document.getElementById("nameEdit").value = $("#name" + id).text();
    document.getElementById("passwordEdit").value = $("#password" + id).text();
}

function openModalWindowDelete(id) {
    document.getElementById("idDelete").value = id;
    document.getElementById("nameDelete").value = $("#name" + id).text();
    document.getElementById("passwordDelete").value = $("#password" + id).text();

}

allUsers()