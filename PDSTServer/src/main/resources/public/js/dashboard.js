$(document).ready(function () {
    currentStaff = JSON.parse(localStorage.getItem("staff"));
    if (currentStaff != undefined) {
        if (currentStaff.roleId == 1 || currentStaff == 2) {
            console.log("init..............: " + currentStaff.token);
            $("#navbar-nav-imgUrl").attr('src', currentStaff.imgUrl);
            $("#navbar-nav-username").html(currentStaff.username);
            $("#pull-lelf-user-img").attr('src', currentStaff.imgUrl);
            $("#pull-lelf-username").html(currentStaff.username);
            $("#dropdown-menu-imgUrl").attr('src', currentStaff.imgUrl);
            $("#dropdown-menu-username").html(currentStaff.username);
            if (currentStaff.roleId == 1) {
                $("#dropdown-menu-role").html("Admin");
            } else {
                $("#dropdown-menu-role").html("Staff");
            }
        } else {
            window.location.href = "403.html";
        }
    }
})

/*
var currentStaff;

function init() {
    currentStaff = JSON.parse(localStorage.getItem("staff"));
    console.log(currentStaff);
    if (currentStaff != undefined) {
        console.log("init..............: " + currentStaff.imgUrl);
        $("#nav_img_url").src = currentStaff.imgUrl.toString();
    }
    imgOnload();
}

function imgOnload() {
    if (currentStaff != undefined) {
        console.log("imgOnLoad..............: " + currentStaff.imgUrl);
        $("#nav_img_url").src = currentStaff.imgUrl.toString();
    }
}*/
