/**
 * @author Susheel Kona
 */
"use strict";

/**
 * @deprecated Remove ?
 */
function toggleNavbar(){
    let navBar = document.getElementById("nav-bar");
    if(navBar.style.display == "block"){
        //$(navBar).hide()
        navBar.style.display = "none"
    } else {
        navBar.style.display = "block"
    }
}

