/**
 * Managing dynamic forms
 *
 * @author Andi Gu
 */

requiredTaskFields = {
    individual: ["name", "dueDate"],
    group: ["name", "dueDate", "group"],
    project: ["name", "dueDate", "project"]
};

$("#add-task-type").change(function () {
    const chosenType = $("#add-task-type").val();

    $.each($("#add-task").children("div"), function (index, value) {
        let id = processId(value.id);
        let elem = $(this);
        let inputElem = $("#add-task-" + id);
        if (requiredTaskFields[chosenType].indexOf(id) != -1) {
            elem.show();
            inputElem.prop('disabled', false);
        }
        else {
            elem.hide();
            inputElem.prop('disabled', true);
        }
    });

});

function processId(id) {
    return id.replace("add-task-", "").replace("-container", "");
}