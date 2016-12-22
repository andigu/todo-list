/**
 * @author Andi Gu
 * @author Susheel Kona
 */
"use strict";

let viewModel = new ActivityViewModel();

$(document).ready(function () {
    ko.applyBindings(viewModel);
    $("#register-button").click(function () {
        setHash("register");
    });

    $(".default-hide").hide();

    setHash(getHash());
    request("/sessions", "GET", {cmd: "userInf"}, (response) => {
        mapUser(response)
    });

    $(".nav-link").click(function () {
        console.log("clicked navbar");
        setHash(this.id.substring(0, this.id.indexOf("-")));
        $(this).parent().children().removeClass("w3-indigo");
        $(this).addClass("w3-indigo");
    });

    $("#hamburger-icon").click(function () {
        $("#nav-bar").toggle();
    });

    //Dropdown
    $("#logout").click(function () {
        request("/logout", "POST", {}, (response) => {
            if (getStatus(response, "success")) {
                console.log("logged out!");
                setHash("login");
                viewModel = new ActivityViewModel(); // Clear data
            }
            else {
                alert("Something went wrong!");
            }

        })
    });

    $("#myprofile").click(function () {
        alert("myprofile")
    });

    $("#add-task-button").click(function () {
        viewModel.getGroups();
        viewModel.getProjects();
        $("#add-task-type").change(); // Fires change just in case
        $("#add-task-container").show();
    });

    $("#add-task-close").click(function () {
        $("#add-task-container").hide();
    });
});
