$(document).ready(function () {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var videoId = url.searchParams.get("videoId");
    console.log('typeOF courseId = ' + typeof courseId);
    currentStaff = JSON.parse(localStorage.getItem("staff"));
    if (currentStaff != undefined && currentStaff != null) {
        $("#btn-sign-out").click(function () {
            localStorage.removeItem("staff");
            window.location.href = "../../login.html";
        })
        if (currentStaff.roleId != 1) {
            $("#admin-authorized").prop('hidden', true);
        }
        if (currentStaff.roleId == 1 || currentStaff.roleId == 2) {
            console.log("init..............: " + currentStaff);
            $("#navbar-nav-imgUrl").attr('src', currentStaff.imgUrl);
            $("#navbar-nav-username").html(currentStaff.username);

            $("#dropdown-menu-imgUrl").attr('src', currentStaff.imgUrl);
            $("#dropdown-menu-username").html(currentStaff.username);
            if (currentStaff.roleId == 1) {
                $("#dropdown-menu-role").html("Admin");
            } else {
                $("#dropdown-menu-role").html("Nhân viên");
            }
            //init course data
            initVideoDetail(parseInt(videoId, 10));

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

            /*clickButtonUpdate(parseInt(videoId, 10));*/

        } else {
            window.location.href = "403.html";
        }
    } else {
        window.location.href = "403.html";
    }
})

/*
function clickButtonUpdate(courseId) {
    $("#btn-video-update").click(function () {
        $("#mUpdateInfo").html("Bạn có muốn thay đổi thông tin!");
        $("#btn-save-change").show();
    })
    $("#btn-save-change").click(function () {
        editVideoDetail(courseId)
    })
}
*/

function editVideoDetail(video) {

    $.ajax({
        url: "/video/editVideoStatusByStaffOrAdmin",
        type: "PUT",
        data: JSON.stringify(video),
        headers: {
            "content-type": "application/json; charset=UTF-8",
            "Authorization": currentStaff.token
        },
        dataType: "json",

    }).done(function (res) {
        console.log(res);

        if (res != null) {
            if (res == true) {
                $("#mUpdateInfo").html("Cập nhật thành công!");
                setTimeout(function () {
                    window.location.reload();
                }, 2000);

            } else {
                $("#mUpdateInfo").html("Cập nhật thất bại!");
            }
        } else {
            console.log("cập nhật thất bại");
        }

    });

}

function initVideoDetail(videoId) {
    $.ajax({
        url: "/video/getVideoDetailById/" + videoId,
        type: "GET",
        headers: {
            "content-type": "application/json; charset=UTF-8",
            "Authorization": currentStaff.token
        },
        dataType: "json",
        success: function (res) {
            if (res != null) {
                console.log(res);
                $("#detail-video-thumbnailImg").attr('src', res.thumnail);
                $("#detail-video-name").html(res.title);
                $("#detail-course-name").html(res.coursename);
                $("#detail-video-numOfView").html(res.numOfView);
                $("#detail-course-trainer").html(res.trainerName);
                if (res.status.toLowerCase() == "active") {
                    $("#detail-video-status").html("Đang hoạt động").css('color', 'green');
                } else {
                    $("#detail-video-status").html("Ngưng hoạt động").css('color', 'red');
                }

                var $video = $("#detail-video-content")[0];
                $video.src = res.content;
                $("#detail-video-content").attr('poster', res.thumnail);
                $video.load();

                $("#btn-profile-change-info").click(function () {
                    $(".change-info").show();
                    $(".init-info").hide();
                    $("#btn-profile-change-info").hide();

                    $("#input-detail-video-name").val(res.title);
                    $("#div-detail-video-status").css('display', 'inline-block');
                    if (res.status != null) {
                        if (res.status.toLowerCase() == "active") {
                            $("#select-detail-video-status").val("active");
                        } else {
                            //$("#rdInActive").iCheck('check');
                            $("#select-detail-video-status").val("inactive");
                        }
                    }
                });

                $("#btn-profile-update").click(function () {
                    if (validData()) {
                        $("#mUpdateInfo").html("Bạn có muốn thay đổi thông tin ?");
                        $("#btn-save-change").show();
                    }
                })

                $("#btn-profile-cancel-update").click(function () {
                    $(".change-info").hide();
                    $(".init-info").show();
                })

                $("#btn-save-change").click(function () {
                    res.title = $("#input-detail-video-name").val()
                    res.status = $("#select-detail-video-status").val();
                    console.log(res);
                    editVideoDetail(res)
                })

                function validData() {
                    if ($("#input-detail-video-name").val().toString().trim() == "") {
                        $("#mUpdateInfo").html("Lỗi: Tên video ko được để trống");
                        $("#btn-save-change").hide();
                        return false;
                    }
                    return true;
                }
            } else {
                console.log("null");
            }
        }
    })
}

function displayVideo(content, title) {
    $("#videoLink").click(function () {
        $("#detail-video-content").attr("src", content);
        $("#myModalLabel").html(title);
    });
}

function stopVideo() {
    var iframe = document.getElementById("detail-video-content");
    iframe.src = "";
}



