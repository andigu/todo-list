/**
 * @author Andi Gu
 */

"use strict";
function inApp(hash) {
    if (hash !== undefined) {
        return !((hash === "login" || hash === "register"));
    } else {
        return !((getHash() === "login" || getHash() === "register"));
    }
}

function getHash() {
    return location.hash.substring(1);
}


function setHash(hash) {
    if (hash === getHash()) {
        $(window).trigger("hashchange");
    } else {
        location.hash = hash;
    }
}

$(window).on("hashchange", function () {
    let hash = getHash();
    $.get("/sessions", {
        "cmd": "ping"
    }, function (response) {
        let loggedIn = getStatus(response, "logged-in");
        if (loggedIn && !inApp(hash)) {
            location.hash = "app";
        } else if (!loggedIn && inApp(hash)) {
            location.hash = "login";
        } else {
            location.hash = hash;
        }
        hash = getHash();
        let elementId = "#" + hash + "-view";
        const body = $("body");
        const loadingDiv = $("#loading");
        if (!inApp(hash)) {
            body.children("div.persist").hide();
        } else {
            body.children("div.persist").show();
        }
        if (hashHandlers.hasOwnProperty(getHash())) {
            hashHandlers[getHash()]();
        }
        body.children("div.content").hide();
        loadingDiv.show(function () {
            body.children(elementId).show(function () {
                loadingDiv.hide();
            });
        });
        viewModel.currentView(getHash());
    });
});