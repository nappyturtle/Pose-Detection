$(document).ready(function () {
    $(".wrapper").css('font-size', '18px');
    currentStaff = JSON.parse(localStorage.getItem("staff"));
    console.log(currentStaff);
    if (currentStaff != undefined && currentStaff != null) {
        if (currentStaff.roleId == 1) {
            $("#li-course-url").hide();
            $("#li-video-url").hide();
            $("#li-category-url").hide();
            $("#course-box-info").hide();
            $("#video-box-info").hide();
            $("#category-box-info").hide();
        }
        if (currentStaff.roleId == 1 || currentStaff.roleId == 2) {
            console.log("init..............: " + currentStaff.token);
            $("#navbar-nav-imgUrl").attr('src', currentStaff.imgUrl);
            //$("#navbar-nav-username").html(currentStaff.username);
            $("#pull-lelf-user-img").attr('src', currentStaff.imgUrl);
            //$("#pull-lelf-username").html(currentStaff.username);
            $("#dropdown-menu-imgUrl").attr('src', currentStaff.imgUrl);
            //$("#dropdown-menu-username").html(currentStaff.username);

            $.ajax({
                url: "/account/update/" + currentStaff.id,
                type: "GET",
                headers: {
                    "content-type": "application/json; charset=UTF-8"
                },
                dataType: "json",
                success: function (res) {
                    if (res.fullname != null) {
                        $(".navbar-username").html(res.fullname);
                    } else {
                        $(".navbar-username").html(res.username);
                    }
                }
            })

            if (currentStaff.roleId == 1) {
                $("#dropdown-menu-role").html("Admin");
            } else {
                $("#dropdown-menu-role").html("Nhân viên");
            }
        } else {
            window.location.href = "403.html";
        }
    } else {
        window.location.href = "403.html";
    }

    $.ajax({
        url: "/account/getDataForDashboard/" + currentStaff.roleId,
        type: "GET",
        headers: {
            "content-type": "application/json; charset=UTF-8",
            "Authorization": currentStaff.token
        },
        dataType: "json",
        success: function (res) {
            if (res != null) {
                $("#total_video").html(res.totalVideo);
                $("#total_course").html(res.totalCourse);
                $("#total_user").html(res.totalUser);
                $("#total-cate").html(res.totalCate);
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
