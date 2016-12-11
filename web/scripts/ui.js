/**
 * @author Susheel Kona
 */
"use strict";

//Navbar functions
function toggleNavbar(){
    let navBar = document.getElementById("nav-bar");
    if(navBar.style.display == "block"){
        //$(navBar).hide()
        navBar.style.display = "none"
    } else {
        navBar.style.display = "block"
    }
}

