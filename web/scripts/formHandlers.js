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
        let inputElem = $("#add-task-" + id);
        if (requiredTaskFieldsIds[chosenType].indexOf(id) != -1) {
            elem.show();
        }
        else {
            elem.hide();
        }
    });

});

function processId(id) {
    return id.replace("add-task-", "").replace("-container", "");
}