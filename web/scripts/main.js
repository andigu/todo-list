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
    const self = this;
    self.user = ko.observable();
    self.login = function (form) {
        $.post("/login", {
            username: form.username.value,
            password: form.password.value
        }, function (response) {
            console.log(response);
            if (response === null) {
                alert("Wrong login");
            }
            else {
                self.user(response);
                location.hash = 'app';
            }
        })
    };
    location.hash = 'login';
    Sammy(function () {
        this.get('#:login', function () {
            self.user(null);
        });

        this.get('#:app', function () {
            alert("hello");
            hideLogin();
        });
    }).run();


}

function hideLogin() {
    $('#login-form').hide();
}

ko.applyBindings(new ActivityViewModel());