const url = "http://localhost:8080/getUser";
let output = ""

function getUser() {

    fetch(url)
        .then(res => res.json())
        .then((user) => {

            output += `
                <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.password}</td>
                <td>${user.roles.map(role => role.name)}</td>
                </tr>
            `;
            document.getElementById("userInfo").innerHTML = output;
        })
}
getUser()