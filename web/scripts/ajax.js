/**
 * @author Andi Gu
 */

function request(url, method, jsonData, success) {
    if (method === "GET") {
        jsonData = jQuery.param(jsonData);
    }
    else if (method === "POST") {
        jsonData = JSON.stringify(jsonData);
    }
    $.ajax({
        url: url,
        method: method,
        contentType: "application/json",
        data: jsonData,
        success: success
    })
}