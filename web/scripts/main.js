/**
 * @author Andi Gu
 */
"use strict";

class ActivityViewModel {
    constructor() {
        this.name = ko.observable();
        this.userName = ko.observable();
        this.id = ko.observable();
        this.displayedTasks = ko.observableArray();
    }

    login(form) {
        const self = this;
        console.log(form, form.elements);
        $.post("/login", {
            "username": form.username.value,
            "password": form.password.value,
            "stay-logged": form.stay.checked
        }, function (response) {
            if (response.hasOwnProperty("user")) {
                console.log(response);
                mapUser(response["user"]);
                if (response.hasOwnProperty("token")) {
                    document.cookie = "token=" + response["token"];
                }
                setHash("app");
                self.getTasks();
            }
            else {
                alert("Wrong login");
            }
        });
    };

    register(form) {
        $.get("/register", {
            "first-name": form.firstname.value,
            "last-name": form.lastname.value,
            "username": form.username.value,
            "password": form.password.value
        }, function (response) {
            if (response.hasOwnProperty("error")) {
                alert(response["error"]);
                console.log(response)
            }
            else {
                mapUser(response);
            }
        });
    }

    getTasks(taskTypes) {
        const self = this;
        if (taskTypes === undefined) {
            taskTypes = ["individual", "group", "project"]
        }
        $.get("/tasks", {
            "user-id": self.id,
            "task-types": taskTypes.toString(),
        }, function (response) {
            self.displayedTasks(response);
            console.log(response);
        });
    };
}

function mapUser(object) {
    console.log(object);
    viewModel.userName(object["username"]);
    viewModel.id(object["id"]);
    viewModel.name(object["name"]);
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
    const loadingDiv = $("#loading");
    body.children("div.content").hide();
    loadingDiv.show(function () {
        body.children(elementId).show(function () {
            loadingDiv.hide();
        });
    });

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
                mapUser(response["user"]);
                setHash("app");
                viewModel.getTasks();
            }
        });
        return true;
    }
    return false;
}

function getHash() {
    return location.hash.substring(1);
}

function setHash(hash) {
    location.hash = hash;
    if (getHash() === "login") {
        checkToken();
    }
    focus(location.hash);
}


$(document).ready(function () {
    ko.applyBindings(viewModel);
    $("#register-button").click(function () {
        setHash("register");
    });

    if (checkToken()) { // TODO perhaps sloppy?
        return;
    }
    if (!(viewModel.id() === undefined || viewModel.id() !== null) ) {
        setHash("app");
    }
    else {
        setHash("login");
    }
});