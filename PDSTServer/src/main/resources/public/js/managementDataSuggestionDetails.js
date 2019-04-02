$(document).ready(function () {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var suggestionId = url.searchParams.get("suggestionId");
    console.log('typeOF suggestionId = ' + typeof suggestionId);
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
            initSuggestionDetail(parseInt(suggestionId, 10));

            editSuggestion(parseInt(suggestionId, 10), currentStaff.token);
        } else {
            window.location.href = "403.html";
        }
    }else {
        window.location.href = "403.html";
    }
})

function editSuggestion(suggestionId, token) {

    console.log('da vao edit course nay ne ');
    var status = "";
    var radioValue = $("input[name='r3-status']").on('ifChecked', function (event) {
        status = $(this).val();
    });

    $('form').submit(function (event) {
        event.preventDefault();
        console.log("You pressed OK!");

        var formData = {
            id: suggestionId,
            status: status
        };
        alert("Your are a - " +  suggestionId + "-" + status);
        var r = confirm("Bạn có muốn thay đổi thông tin không?");
        if (r == true) {
            $.ajax({
                url: "/suggestion/editStatusSuggestionByStaffOrAdmin",
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
                        alert("Cập nhật thành công!");
                        location.reload();
                    } else {
                        alert("Cập nhật thất bại!")
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

function initSuggestionDetail(suggestionId) {
    $.ajax({
        url: "/suggestion/getSuggestionById/" + suggestionId,
        type: "GET",
        headers: {
            "content-type": "application/json; charset=UTF-8",
            "Authorization": currentStaff.token
        },
        dataType: "json",
        success: function (res) {
            if (res != null) {
                console.log(res);
                $("#detail-suggestion-thumbnailImg").attr('src', res.thumnailUrl);
                $("#detail-suggestion-video-name").val(res.videoname);
                $("#detail-suggestion-trainee").val(res.accountname);
                if (res.status === "active") {
                    $("#rdActive").iCheck('check');
                    //$("#rdActive").iCheck('check').val("active");
                    $("#rdActive").val(res.status);
                    $("#rdProcessing").val("processing");
                    $("#rdInActive").val("inactive");
                }else if(res.status === "processing"){
                    $("#rdProcessing").iCheck('check');
                    //$("#rdProcessing").iCheck('check').val("processing");
                    $("#rdProcessing").val(res.status);
                    $("#rdActive").val("active");
                    $("#rdInActive").val("inactive");
                } else {
                    $("#rdInActive").iCheck('check');
                    //$("#rdInActive").iCheck('check').val("inactive");
                    $("#rdInActive").val(res.status);
                    $("#rdProcessing").val("processing");
                    $("#rdActive").val("active");
                }
                $("#suggestion-to-suggestionDetail").attr("href", "../suggestiondetail/layout.html?suggestionId="+suggestionId);

            } else {
                console.log("null");
            }
        }
    })
}

