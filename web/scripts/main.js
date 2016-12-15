/**
 * @author Andi Gu
 * @author Susheel Kona
 */
"use strict";

const viewModel = new ActivityViewModel();

$(document).ready(function () {
    ko.applyBindings(viewModel);
    $("#register-button").click(function () {  // TODO Put this in html
        setHash("register");
    });

    setHash(getHash());// TODO sloppy
    $.get("/sessions", {"cmd": "user-inf"}, function (response) {
        mapUser(response);
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
        alert("Logout")
    });

    $("#myprofile").click(function () {
        alert("myprofile")
    });
});
