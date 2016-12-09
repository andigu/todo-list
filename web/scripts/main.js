/**
 * @author Andi Gu
 */
"use strict";

class ActivityViewModel {
    constructor() {
        this.name = ko.observable();
        this.userName = ko.observable();
        this.individualTasks = ko.observableArray();
        this.groupTasks = ko.observableArray();
        this.projectTasks = ko.observableArray();
        this.groups = ko.observableArray();
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
        if (taskTypes === undefined) {
            taskTypes = ["individual", "group", "project"]
        }
        $.get("/tasks", {
            "task-types": taskTypes.toString(),
        }, function (response) {
            mapTasks(response);
        });
    };

    getGroups() {
        const self = this;
        $.get("/groups", {}, function (response) {
            self.groups(response);
        });
    }

    goTo(val, event) {
        let id = event.target.id;
        id = id.substring(0, id.indexOf("-"));
        setHash(id);
    }
}


function mapUser(object) {
    viewModel.userName(object["username"]);
    viewModel.name(object["name"]);
}

function mapTasks(object) {
    viewModel.individualTasks(object["individual"]);
    viewModel.groupTasks(object["group"]);
    viewModel.projectTasks(object["project"]);
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
    if (!inApp()) {
        body.children("div.persist").hide();
    }
    else {
        body.children("div.persist").show();
    }
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

function hasToken() {
    return mapCookies().hasOwnProperty("token")
}

function inApp(hash) {
    if (hash !== undefined) {
        return !((hash === "login" || hash === "register"));
    }
    else {
        return !((getHash() === "login" || getHash() === "register"));
    }
}

function getHash() {
    return location.hash.substring(1);
}

function setHash(hash) {
    console.log(hash);
    $.get("/sessions", {
        "cmd": "ping"
    }, function (response) {
        if (response["logged-in"]) {
            updatePage(true, hash);
        } else {
            if (!hasToken()) {
                updatePage(false, hash);
            } else {
                $.post("/login", {
                    "token": mapCookies()["token"]
                }, function (response) {
                    if (response["user"] != null) {
                        mapUser(response["user"]);
                        updatePage(true, hash);
                    }
                    else {
                        updatePage(false, hash);
                    }
                });
            }
        }
    });
}

function updatePage(loggedIn, hash) {
    if (loggedIn && !inApp(hash)) {
        location.hash = "app";
    } else if (!loggedIn && inApp(hash)) {
        location.hash = "login";
    } else {
        location.hash = hash;
    }
    focus(hash);
    if (hashHandlers.hasOwnProperty(getHash())) {
        hashHandlers[getHash()]();
    }
}

$(window).on("hashchange", function() {
    setHash(getHash()); // TODO More sloppy!
});

$(document).ready(function () {
    ko.applyBindings(viewModel);
    $("#register-button").click(function () {
        setHash("register");
    });

    setHash(getHash());// TODO sloppy
    $.get("/sessions", {"cmd": "user-inf"}, function (response) {
        mapUser(response);
    })
});

const hashHandlers = {
    "app": viewModel.getTasks,
    "groups": viewModel.getGroups
};