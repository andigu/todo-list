/**
 * @author Andi Gu
 */
"use strict";

requirejs.config({
    //By default load any module IDs from js/lib
    baseUrl: 'js/lib',
});



class ActivityViewModel {
    constructor() {
        this.user = null;
        this.displayedTasks = ko.observableArray();
    }

    login(form) {
        const self = this;
        $.post("/login", {
            "username": form.username.value,
            "password": form.password.value,
            "stay-logged": form.stay.checked
        }, function (response) {
            if (response.hasOwnProperty("user")) {
                console.log(response);
                self.user = response["user"];
                if (response.hasOwnProperty("token")) {
                    document.cookie = "token=" + response["token"];
                }
                location.hash = "app";
                self.getTasks();
            }
            else {
                alert("Wrong login");
            }
        });
    };


    register(form) {
        const self = this;
        $.get("/register", {
            "first-name": form.firstname.value,
            "last-name": form.lastname.value,
            "username": form.username.value,
            "password": form.password.value
        }, function (response) {
            if (response.hasOwnProperty("user")) {
                self.user = response["user"];
            }
            else {
                alert(response["error"]);
                console.log(response)
            }
        });
    }

    getTasks(taskTypes) {
        const self = this;
        if (taskTypes === undefined) {
            taskTypes = ["individual", "group", "project"]
        }
        $.get("/tasks", {
            "user-id": self.user.id,
            "task-types": taskTypes.toString(),
        }, function (response) {
            self.displayedTasks(response);
            console.log(response);
        });
    };
}

const viewModel = new ActivityViewModel();

function focus(elementId) {
    if (elementId.charAt(0) !== "#") {
        elementId = "#" + elementId;
    }
    if (elementId.substr(elementId.length - 5) !== "-view") {
        elementId += "-view"
    }
    const body = $("body");
    body.children("div").hide();
    body.children(elementId).show();
}

function mapCookies() {
    const map = {};
    const split = document.cookie.split(";");
    for (let i = 0; i < split.length; i++) {
        let temp = split[i].split("=");
        map[temp[0]] = temp[1];
    }
    return map;
}

function checkToken() {
    if (mapCookies().hasOwnProperty("token")) {
        $.post("/login", {
            "token": mapCookies()["token"]
        }, function (response) {
            if (response["user"] != null) {
                viewModel.user = response["user"];
                location.hash = "app";
                viewModel.getTasks();
            }
        });
    }
}

function currentHash() {
    return location.hash.substring(1);
}

$(document).ready(function () {
    ko.applyBindings(viewModel);
    checkToken();
    $(window).on("hashchange", function () {
        if (currentHash() === "login") {
            checkToken();
        }
        focus(location.hash);
    });

    $("#register-button").click(function () {
        location.hash = "register";
    });

    if (viewModel.user !== undefined && viewModel.user !== null) {
        location.hash = "app";
        focus("app")
    }
    else {
        if (currentHash() !== "login") {
            location.hash = "login";
        }
        else {
            focus("login");
        }
    }
});
