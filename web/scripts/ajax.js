/**
 * @author Andi Gu
 */

function request(url, method, jsonData, success) {
    if (method === "GET") {
        if (!jQuery.isPlainObject(jsonData)) {
            throw new Error("No nested objects allowed")
        }
        jsonData = jQuery.param(jsonData);
    }
    else if (method === "POST") {
        jsonData = JSON.stringify(jsonData);
    }
    console.log(jsonData);
    $.ajax({
        url: url,
        method: method,
        contentType: "application/json",
        data: jsonData,
        success: success
    })
}