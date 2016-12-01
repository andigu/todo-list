/**
 * @author Andi Gu
 */
"use strict";

class ActivityViewModel {
    constructor() {
        const self = this;
        self.user = null;
        self.displayedTasks = ko.observableArray();
        self.login = function (form) {
            $.post("/login", {
                'username': form.username.value,
                'password': form.password.value
            }, function (response) {
                if (response == null) {
                    alert("Wrong login");
                }
                else {
                    self.user = response;
                    location.hash = 'app';
                    self.getTasks();
                }
            })
        };
    }

    getTasks(taskTypes) {
        const self = this;
        if (taskTypes === undefined) {
            taskTypes = ['individual', 'group', 'project']
        }
        let result = null;
        $.get("/tasks", {
            'user-id': self.user.id,
            'task-types': taskTypes.toString(),
        }, function (response) {
            self.displayedTasks(response);
            console.log(response);
        });
    };
}


function focus(elementId) {
    if (elementId.charAt(0) !== '#') {
        elementId = '#' + elementId;
    }
    if (elementId.substr(elementId.length - 5) !== '-view') {
        elementId += '-view'
    }
    const body = $('body');
    body.children('div').hide();
    body.children(elementId).show();
}

$(document).ready(function () {
    ko.applyBindings(new ActivityViewModel());
    $(window).on('hashchange', function () {
        focus(location.hash + "-view");
    });
    if (location.hash !== '#login') {
        location.hash = 'login';
    }
    else {
        focus('#login-view');
    }
});
