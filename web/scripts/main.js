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
                'password': form.password.value,
                'stay-logged': form.stay.value
            }, function (response) {
                if (response['user'] == null) {
                    alert('Wrong login!');
                }
                else {
                    console.log(response);
                    self.user = response['user'];
                    if (response.hasOwnProperty('token')) {
                        document.cookie = 'token=' + response['token'];
                    }
                    location.hash = 'app';
                    self.getTasks();
                }
            });
        };
    }

    getTasks(taskTypes) {
        const self = this;
        if (taskTypes === undefined) {
            taskTypes = ['individual', 'group', 'project']
        }
        $.get("/tasks", {
            'user-id': self.user.id,
            'task-types': taskTypes.toString(),
        }, function (response) {
            self.displayedTasks(response);
            console.log(response);
        });
    };
}

const viewModel = new ActivityViewModel();

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

function mapCookies() {
    const map = {};
    const split = document.cookie.split(';');
    for (let i = 0; i < split.length; i++) {
        let temp = split[i].split("=");
        map[temp[0]] = temp[1];
    }
    return map;
}


$.holdReady(true);
if (mapCookies().hasOwnProperty("token")) {
    $.post("/login", {
        'token': mapCookies()["token"]
    }, function (response) {
        if (response['user'] != null) {
            viewModel.user = response['user'];
            location.hash = 'app';
            viewModel.getTasks();
        }
        $.holdReady(false)
    });
}

$(document).ready(function () {
    ko.applyBindings(viewModel);
    $(window).on('hashchange', function () {
        focus(location.hash + "-view");
    });
    if (viewModel.user != undefined) {
        location.hash = 'app';
        focus('#app')
    }
    else {
        if (location.hash !== '#login') {
            location.hash = 'login';
        }
        else {
            focus('#login-view');
        }
    }
});
