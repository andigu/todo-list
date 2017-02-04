/**
 * @author Andi Gu
 * @author Susheel Kona
 */

"use strict";

var titleMap = {
    app: "Dashboard",
    groups: "My Groups",
    projects: "My Projects",
    login: "Login",
    group: function () {
        return (viewModel.currentGroupName())
    }
};

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

    });


    $("#tab-name").text(titleMap[getHash()]);

    //Is there a better way to do this?
    if(getHash() === "group" || getHash() === "project") document.title = "Todolist | "+(titleMap[getHash()])();
    else document.title = "Todolist | "+(titleMap[getHash()]);

    $("#nav-bar").hide();
});
