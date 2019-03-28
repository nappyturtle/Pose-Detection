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
            $("#pull-lelf-user-img").attr('src', currentStaff.imgUrl);
            $("#pull-lelf-username").html(currentStaff.username);
            $("#dropdown-menu-imgUrl").attr('src', currentStaff.imgUrl);
            $("#dropdown-menu-username").html(currentStaff.username);
            if (currentStaff.roleId == 1) {
                $("#dropdown-menu-role").html("Admin");
            } else {
                $("#dropdown-menu-role").html("Staff");
            }
            //init course data
            initVideoDetail(parseInt(videoId, 10));

            editVideoDetail(parseInt(videoId, 10), currentStaff.token);
        } else {
            window.location.href = "403.html";
        }
    }else {
        window.location.href = "403.html";
    }
})

function editVideoDetail(videoId, token) {

    console.log('da vao edit course nay ne ');

    $('form').submit(function (event) {
        event.preventDefault();
        console.log("You pressed OK!");
        var radioValue = $("input[name='r3-status']").iCheck('update')[0].checked;
        console.log('radioValue = ' + radioValue);
        var status = "";
        if (radioValue) {
            status = "active"
        } else {
            status = "inactive";
        }
        var formData = {
            id          : videoId,
            title       : $("#detail-video-name").val(),
            numOfView   : parseInt($('#detail-video-numOfView').val(), 10),
            status      : status
        };
        //alert("Your are a - " + title + "-" + numOfView + "-" + status);
        var r = confirm("Bạn có muốn thay đổi thông tin không?");
        if (r == true) {
            $.ajax({
                url: "/video/editVideoStatusByStaffOrAdmin",
                type: "PUT",
                data: JSON.stringify(formData),
                headers: {
                    "content-type": "application/json; charset=UTF-8",
                    "Authorization": token
                },
                dataType: "json",

            }).done(function (res) {
                console.log(res);
                if (res != null) {
                    if (res == true) {
                        alert("Cập nhập thành công!");
                        location.reload();
                    } else {
                        alert("Cập nhập thất bại!")
                    }
                } else {
                    console.log("update thật bại");
                }

            });


        } else {
            console.log("You pressed Cancel!");
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
                } else {
                    //$("#rdInActive").prop("checked", true);
                    $("#rdInActive").iCheck('check');
                    $("#rdInActive").val(res.status);
                }
                // $('#detail-video-content source').attr('src', res.content);
                // $("#detail-video-content")[0].load();
                $('#detail-video-content').attr('src', res.content);

            } else {
                console.log("null");
            }
        }
    })
}

