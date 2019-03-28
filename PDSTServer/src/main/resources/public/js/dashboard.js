$(document).ready(function () {
    currentStaff = JSON.parse(localStorage.getItem("staff"));
    console.log(currentStaff);
    if (currentStaff != undefined && currentStaff != null) {
        if (currentStaff.roleId != 1) {
            $("#admin-authorized").prop('hidden', true);
        }
        if (currentStaff.roleId == 1 || currentStaff.roleId == 2) {
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
    } else {
        window.location.href = "403.html";
    }

    $.ajax({
        url: "/account/getDataForDashboard",
        type: "GET",
        headers: {
            "content-type": "application/json; charset=UTF-8"
        },
        dataType: "json",
        success: function (res) {
            if (res != null) {
                $("#total_video").html(res.totalVideo);
                $("#total_course").html(res.totalCourse);
                $("#total_user").html(res.totalUser);
            } else {
                console.log("null");
            }
        }
    })

    $("#btn-sign-out").click(function () {
        localStorage.removeItem("staff");
        window.location.href = "../login.html";
    })

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
