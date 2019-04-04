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
                $("#dropdown-menu-role").html("Staff");
            }
            //init course data
            initVideoDetail(parseInt(videoId, 10));

            clickButtonUpdate(parseInt(videoId, 10));


        } else {
            window.location.href = "403.html";
        }
    } else {
        window.location.href = "403.html";
    }
})
function clickButtonUpdate(courseId) {
    $("#btn-video-update").click(function () {
        $("#mUpdateInfo").html("Bạn có muốn thay đổi thông tin!");
        $("#btn-save-change").show();
    })
    $("#btn-save-change").click(function () {
        editVideoDetail(courseId)
    })
}
function editVideoDetail(videoId) {

    console.log('da vao edit course nay ne ');

    var radioValue = $("input[name='r3-status']").iCheck('update')[0].checked;
    console.log('radioValue = ' + radioValue);
    var status = "";
    if (radioValue) {
        status = "active"
    } else {
        status = "inactive";
    }
    var formData = {
        id: videoId,
        //title: $("#detail-video-name").val(),
        //numOfView: parseInt($('#detail-video-numOfView').val(), 10),
        status: status
    };

    $.ajax({
        url: "/video/editVideoStatusByStaffOrAdmin",
        type: "PUT",
        data: JSON.stringify(formData),
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
                    $('#modal-info').modal('hide');
                    $("#btn-save-change").hide();
                    location.reload();
                }, 2000);

            } else {
                $("#mUpdateInfo").html("Cập nhật thất bại!");
                setTimeout(function () {
                    $('#modal-info').modal('hide');
                    $("#btn-save-change").hide();
                    location.reload();
                }, 2000);

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
                $("#detail-video-name").val(res.title);
                $("#detail-course-name").val(res.coursename);
                $("#detail-video-numOfView").val(res.numOfView);
                if (res.status.toLowerCase() == "active") {
                    //$("#rdActive").prop("checked", true);
                    $("#rdActive").iCheck('check');
                    $("#rdActive").val(res.status);
                    $("#rdInActive").val("inactive");
                } else {
                    //$("#rdInActive").prop("checked", true);
                    $("#rdInActive").iCheck('check');
                    $("#rdInActive").val(res.status);
                    $("#rdActive").val("active");
                }
                // $('#detail-video-content source').attr('src', res.content);
                // $("#detail-video-content")[0].load();
                // $('#videoLink').attr('href', res.content);
                // $('.mfp-hidden').attr("style", "display: none !important");

                // var content = $('#videoLink').attr('href');
                // console.log(content);
                // displayVideo(content);
                //$("#videoLink").attr("href",res.content);
                displayVideo(res.content, res.title);

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



