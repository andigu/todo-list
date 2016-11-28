/**
 * @author Andi Gu
 */
"use strict";

class ActivityViewModel {
    constructor() {
        const self = this;
        self.user = ko.observable();
        self.login = function (form) {
            $.post("/login", {
                username: form.username.value,
                password: form.password.value
            }, function (response) {
                console.log(response);
                if (response == null) {
                    alert("Wrong login");
                }
                else {
                    self.user(response);
                    location.hash = 'app';
                }
            })
        };

        Sammy(function () {
            this.get('#login', function () {
            });

            this.get('#app', function () {
            });
        }).run('#login');
    }
}


function focus(elementId) {
    const body = $('body');
    body.children('div').hide();
    body.children(elementId).show();
}
$(document).ready(function () {
    ko.applyBindings(new ActivityViewModel());
    $(window).on('hashchange', function () {
        focus(location.hash + "-view");
    });
});
