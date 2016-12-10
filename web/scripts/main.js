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
        this.projects = ko.observableArray();
    }

    login(form) {
        const self = this;
        $.post("/login", {
            "username": form.username.value,
            "password": form.password.value,
            "stay-logged": form.stay.checked
        }, function (response) {
            if (response.hasOwnProperty("user")) {
                mapUser(response["user"]);
                if (response.hasOwnProperty("token")) {
                    document.cookie = "token=" + response["token"];
                }
                setHash("app");
                self.getTasks();
            } else {
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
            } else {
                mapUser(response);
            }
        });
    }

    getTasks(taskTypes) {
        let json = {};
        if (taskTypes !== undefined) {
            json["task-types"] = taskTypes.toString();
        }
        $.get("/tasks", json, function (response) {
            mapTasks(response);
        });
    };

    getGroups() {
        const self = this;
        $.get("/groups", {}, function (response) {
            mapObject(response, "groups", self.groups);
        });
    }

    getProjects() {
        const self = this;
        $.get("/projects", {}, function (response) {
            mapObject(response, "projects", self.projects);
        });
    }
}

function hasProperty(object, property) {
    return property in object
}

function getStatus(object, name) {
    if (hasProperty(object, "status")) {
        object = object["status"];
    }
    if (object["name"] === name) {
        return object["status"]
    }
}

function mapUser(object) {
    if (hasProperty(object, "user")) {
        object = object["user"];
    }
    viewModel.userName(object["username"]);
    viewModel.name(object["name"]);
}

function mapTasks(object) {
    if (hasProperty(object, "tasks")) {
        object = object["tasks"];
    }
    viewModel.individualTasks(object["individual"]);
    viewModel.groupTasks(object["group"]);
    viewModel.projectTasks(object["project"]);
}

function mapObject(object, name, observable) {
    if (hasProperty(object, name)) {
        observable(object[name]);
    }
}

const viewModel = new ActivityViewModel();

function inApp(hash) {
    if (hash !== undefined) {
        return !((hash === "login" || hash === "register"));
    } else {
        return !((getHash() === "login" || getHash() === "register"));
    }
}

function getHash() {
    return location.hash.substring(1);
}

function setHash(hash) {
    if (hash === getHash()) {
        $(window).trigger("hashchange");
    } else {
        location.hash = hash;
    }
}

$(window).on("hashchange", function () {
    let hash = getHash();
    $.get("/sessions", {
        "cmd": "ping"
    }, function (response) {
        let loggedIn = getStatus(response, "logged-in");
        if (loggedIn && !inApp(hash)) {
            location.hash = "app";
        } else if (!loggedIn && inApp(hash)) {
            location.hash = "login";
        } else {
            location.hash = hash;
        }
        hash = getHash();
        let elementId = "#" + hash + "-view";
        const body = $("body");
        const loadingDiv = $("#loading");
        if (!inApp(hash)) {
            body.children("div.persist").hide();
        } else {
            body.children("div.persist").show();
        }
        if (hashHandlers.hasOwnProperty(getHash())) {
            hashHandlers[getHash()]();
        }
        body.children("div.content").hide();
        loadingDiv.show(function () {
            body.children(elementId).show(function () {
                loadingDiv.hide();
            });
        });
    });
});

$(document).ready(function () {
    ko.applyBindings(viewModel);
    $("#register-button").click(function () {
        setHash("register");
    });

    setHash(getHash());// TODO sloppy
    $.get("/sessions", {"cmd": "user-inf"}, function (response) {
        mapUser(response);
    });

    $(".nav-link").click(function () {
        setHash(this.id.substring(0, this.id.indexOf("-")));
    });
});

const hashHandlers = {
    "app": function () {
        viewModel.getTasks()
    },
    "groups": function () {
        viewModel.getGroups()
    },
    "projects": function () {
        viewModel.getProjects();
    }
};