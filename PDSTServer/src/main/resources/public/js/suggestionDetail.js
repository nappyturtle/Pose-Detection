$(document).ready(function () {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var suggestionDetailId = url.searchParams.get("suggestionDetailId");
    console.log('typeOF suggestionDetailId = ' + typeof suggestionDetailId);
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
            initSuggestionDetail(parseInt(suggestionDetailId, 10));

            editSugestionDetail(parseInt(suggestionDetailId, 10), currentStaff.token);
        } else {
            window.location.href = "403.html";
        }
    }else {
        window.location.href = "403.html";
    }
})

function editSugestionDetail(suggestionDetailId, token) {

    console.log('da vao edit course nay ne ');
    var status = "";
    var radioValue = $("input[name='r3-status']").on('ifChecked', function (event) {
        status = $(this).val();
    });

    $('form').submit(function (event) {
        event.preventDefault();
        console.log("You pressed OK!");
        // var radioValue = $("input[name='r3-status']").iCheck('update')[0].checked;
        //
        // var status = "";
        // if (radioValue) {
        //     status = "active"
        // } else {
        //     status = "inactive";
        // }
        var formData = {
            id              : suggestionDetailId,
            description     : $("#suggestionDetail-description").val(),
            comment:        $("#suggestionDetail-comment").val(),
            status          : status
        };
        //alert("Your are a - " + courseId + "-" + coursename + "-" + categoryId + "-" + price + "-" + status);
        var r = confirm("Bạn có muốn thay đổi thông tin không?");
        if (r == true) {
            $.ajax({
                url: "/suggestiondetail/editStatusSuggestionDetailByStaffOrAdmin",
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

function initSuggestionDetail(suggestionDetailId) {
    $.ajax({
        url: "/suggestiondetail/getSuggestionDetailById/" + suggestionDetailId,
        type: "GET",
        headers: {
            "content-type": "application/json; charset=UTF-8",
            "Authorization": currentStaff.token
        },
        dataType: "json",
        success: function (res) {
            if (res != null) {
                console.log(res);
                $("#img-trainer").attr('src', res.standardImgUrl);
                $("#img-trainee").attr('src', res.imgUrl);
                $("#suggestionDetail-description").val(res.description);
                $("#suggestionDetail-comment").val(res.comment);
                if (res.status === "active") {
                    $("#rdActive").iCheck('check');
                    $("#rdActive").val(res.status);
                    $("#rdProcessing").val("processing");
                    $("#rdInActive").val("inactive");
                }else if(res.status === "processing"){
                    $("#rdProcessing").iCheck('check');
                    $("#rdProcessing").val(res.status);
                    $("#rdActive").val("active");
                    $("#rdInActive").val("inactive");
                } else {
                    $("#rdInActive").iCheck('check');
                    $("#rdInActive").val(res.status);
                    $("#rdProcessing").val("processing");
                    $("#rdActive").val("active");
                }

            } else {
                console.log("null");
            }
        }
    })
}

