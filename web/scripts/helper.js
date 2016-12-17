/**
 * @author Andi Gu
 */

"use strict";
function hasProperty(object, property) {
    return property in object
}

function getStatus(object, name) {
    if (hasProperty(object, "status")) {
        object = object.status;
    }
    if (object["name"] === name) {
        return object.status
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
    let individualTasks = [];
    let groupTasks = [];
    let projectTasks = [];
    for (let i = 0; i < object.length; i++) { // TODO quite sloppy - but no alternative solution..?
        if (hasProperty(object[i], "owner")) {
            individualTasks.push(object[i]);
        }
        else if (hasProperty(object[i], "project")) {
            projectTasks.push(object[i]);
        }
        else if (hasProperty(object[i], "group")) {
            groupTasks.push(object[i]);
        }
    }
    viewModel.individualTasks(individualTasks);
    viewModel.groupTasks(groupTasks);
    viewModel.projectTasks(projectTasks);
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