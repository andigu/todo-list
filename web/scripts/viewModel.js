/**
 * @author Andi Gu
 */

"use strict";

class ActivityViewModel {
    constructor() {
        this.userId = null;
        this.name = ko.observable();
        this.userName = ko.observable();
        this.individualTasks = ko.observableArray();
        this.groupTasks = ko.observableArray();
        this.projectTasks = ko.observableArray();
        this.groups = ko.observableArray();
        this.availableGroups = ko.observableArray();
        this.projects = ko.observableArray();
        this.notifications = ko.observableArray([
            new Notification("Name", "This is a sample notification body"),
            new Notification("Second", "This is the second sample notification body")
        ]);

        //Current group stuff
        this.currentGroupName = ko.observable();
        this.currentGroupId = ko.observable();
        this.currentGroupTasks = ko.observableArray();
        this.currentGroupUsers = ko.observableArray();
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
                    //alert("Wrong login");
                    $("#improper-creds-msg").show();
                }
            });


        setTimeout(function () {
            //location.reload();
        }, 1000);


    };

    register(form) {
        request("/register", "POST", {
            user: {
                firstName: form.firstName.value,
                lastName: form.lastName.value,
                username: form.username.value,
                password: form.password.value,
                confirmPass: form.confirmPassword.value,
                email: form.email.value
            },
        }, (response) => {
            if (response.hasOwnProperty("error")) {
                console.log(response["error"].message);
                $("#improper-register-msg").children("h6").text(response["error"].message);
                $("#improper-register-msg").show();
            } else {
                mapUser(response["data"]);
                setHash("app");
                console.log("registration success!")
                this.getTasks();
            }
        });
    }

    fetchUserdata () {
        request("/sessions", "GET", {cmd: "userInf"}, response => {
            mapUser({"user": response["data"]});
        })
    }

    getTasks(taskTypes, parentId) {
        let filters = {};
        if (taskTypes !== undefined) {
            filters["taskTypes"] = taskTypes.toString();

            //Specifies which group/project to get tasks from
            if (parentId !== undefined) {
                filters["parentId"] = parentId.toString();
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
        request("/groups", "GET", {joined: this.userId}, (response) => {
            this.groups(response.data);
        });
    }



    getAvailableGroups() {
        request("/groups", "GET", {notJoined: this.userId}, response => {
            this.availableGroups(response.data);
        });
    }

    getProjects() {
        request("/projects", "GET", {}, (response) => {
            this.projects(response.data);
        });
    }

    joinGroup(group) {
        request("/groups", "GET", {cmd: "joinGroup", groupId: group.id}, response => {
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
        $(form)[0].reset();
    }


    expandAccordion(viewModel, event) {
        const accordion = $(event.target).next()[0];
        const arrow = $(event.target).find("i")[0];
        if (accordion.className.indexOf("w3-show") == -1) {
            viewModel.getGroups();
            arrow.className = arrow.className.replace(" fa-caret-down", " fa-caret-up");
            accordion.className += " w3-show";
            accordion.previousElementSibling.className += " w3-indigo";
        } else {
            arrow.className = arrow.className.replace(" fa-caret-up", " fa-caret-down");
            accordion.className = accordion.className.replace(" w3-show", "");
            accordion.previousElementSibling.className =
                accordion.previousElementSibling.className.replace(" w3-indigo", "");
        }
    }

    groupClicked(group) {
        console.log(group);
        mapGroup(group);
        setHash("group");
        $(".nav-link").removeClass("w3-indigo");
    }
}

class Notification {
    constructor(name, content) {
        this.name = name;
        this.content = content;
    }
}