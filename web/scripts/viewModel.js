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

        //Used purely for ui
        this.currentView = ko.observable("App");

        //Do something better
        this.selectedGroupID = null;
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