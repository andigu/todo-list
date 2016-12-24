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

    getTasks(taskTypes, parentID) {
        let filters = {};
        if (taskTypes !== undefined) {
            filters["taskTypes"] = taskTypes.toString();

            //Specifies which group/project to get tasks from
            if(parentID !== undefined){
                filters["parentID"] = parentID.toString();
            }
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
                $("#add-task-container").hide();
                response = response["data"];
                if (hasProperty(response, "group")) {
                    this.groupTasks.push(response);
                }
                else if (hasProperty(response, "project")) {
                    this.projectTasks.push(response);
                }
                else {
                    this.individualTasks.push(response);
                }
            });

    }
}

