/**
 * @author Andi Gu
 */

class User {
    constructor(parameter) {
        if (typeof parameter == parseInt(parameter)) {
            this.id = parameter;
        }
        else {
            this.id = parameter.id;
        }
    }
}

function ActivityViewModel() {
    this.login = function (form) {
        $.get("/login", {
            username: form.username.value,
            password: form.password.value
        }, function (response) {
            if (response === null) {
                alert("Wrong login");
            }
            else {
                response = new User(response);
                alert("ID: " + response.id);
            }
        })
    }
}

ko.applyBindings(new ActivityViewModel());