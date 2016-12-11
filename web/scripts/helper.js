/**
 * @author Andi Gu
 */

"use strict";
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