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
        this.selectedGroupID = ko.observable();
    }

    login(form) {
        request("/login",
            "POST", {
                user: {
                    username: form.username.value,
                    password: form.password.value
                },
                stayLogged: form.stay.checked
            }, (response) => {
                console.log(response);
                if (response.hasOwnProperty("data")) {
                    mapUser(response["data"]);
                    setHash("app");
                    this.getTasks();
                } else {
                    alert("Wrong login");
                }
            });
    };

    register(form) {
        request("/register", "GET", {
            "firstName": form.firstName.value,
            "lastName": form.lastName.value,
            "username": form.username.value,
            "password": form.password.value
        }, (response) => {
            if (response.hasOwnProperty("error")) {
                alert(response["error"]);
            } else {
                mapUser(response);
            }
        });
    }

    getTasks(taskTypes) {
        let filters = {};
        if (taskTypes !== undefined) {
            filters["taskTypes"] = taskTypes.toString();
        }
        request("/tasks",
            "GET",
            {filters: filters},
            (response) => {
                mapTasks(response["data"])
            }
        )
    };

    getGroups() {
        request("/groups", "GET", {}, (response) => {
            this.groups(response.data);
        });
    }

    getProjects() {
        request("/projects", "GET", {}, (response) => {
            this.projects(response.data);
        });
    }

    addTask(form) {
        console.log(form);
        request("/tasks", "POST",
            {
                taskType: form.taskType.value,
                task: {
                    name: form.name.value,
                    dueDate: form.dueDate.value,
                    groupId: form.group.value,
                    projectId: form.project.value
                }
            },
            (response) => {
                console.log(response);
            });

    }


    groupClicked(group) {
        alert("Not implemented");
    }
}

