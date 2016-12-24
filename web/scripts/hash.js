/**
 * @author Andi Gu
 */

"use strict";
function inApp(hash) {
    if (hash !== undefined) {
        return !((hash === "login" || hash === "register" || hash === ""));
    }
}

function getHash() {
    return location.hash.substring(1);
}


function setHash(hash) {
    if (hash !== getHash()) {
        location.hash = hash;
    }
    $(window).trigger("hashchange");
}

$(window).on("hashchange", function () {
    let hash = getHash();
    request("/sessions", "GET", {cmd: "ping"}, (response) => {
        let loggedIn = getStatus(response, "loggedIn");
        if (loggedIn && !inApp(hash)) {
            location.hash = "app";
        } else if (!loggedIn) {
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
        $("#tab-name").text(getHash());
        $("#nav-bar").hide();
    });
});