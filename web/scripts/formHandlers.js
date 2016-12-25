/**
 * Managing dynamic forms
 *
 * @author Andi Gu
 */

requiredTaskFieldsIds = {
    individual: ["name", "due-date"],
    group: ["name", "due-date", "group"],
    project: ["name", "due-date", "project"]
};

$("#add-task-type").change(function () {
    const chosenType = $("#add-task-type").val();

    $.each($("#add-task").children("div"), function (index, value) {
        let id = processId(value.id);
        let elem = $(this);
        if (requiredTaskFieldsIds[chosenType].indexOf(id) != -1) {
            elem.show();
        }
        else {
            elem.hide();
        }
    });
});

$("#add-task-group").change(function() {
    if (this.value.length === 0 && $(this).is(":visible")) {
        this.setCustomValidity("You must choose a group");
    }
    else {
        this.setCustomValidity("");
    }
});

$("#add-task-project").change(function() {
    if (this.value.length === 0 && $(this).is(":visible")) {
        this.setCustomValidity("You must choose a project");
    }
    else {
        this.setCustomValidity("");
    }
});


function processId(id) {
    return id.replace("add-task-", "").replace("-container", "");
}