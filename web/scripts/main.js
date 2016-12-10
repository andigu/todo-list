/**
 * @author Andi Gu
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
        setHash(this.id.substring(0, this.id.indexOf("-")));
    });
});
