var editor;

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
            initSuggestionDetail();
        } else {
            window.location.href = "403.html";
        }
    } else {
        window.location.href = "403.html";
    }

    function initSuggestionDetail() {
        $.ajax({
            url: "/suggestiondetail/getAllSuggestionDetailByStaffOrAdmin/" + suggestionId,
            type: "GET",
            headers: {
                "Authorization": currentStaff.token
            },
            success: function (res) {
                console.log(res);

                for (var i = 0; i < res.length; i++) {

                    var div = "<div class='col-md-10' style='margin-bottom: 10px'>" +
                        "<div class='row'>" +
                        "<div class='col-md-2'></div>" +
                        "<div class='col-md-4' style='margin: 10px'>" +
                        "<p style='text-align: center'>Hình từ video mẫu</p>" +
                        "<img class='card-img-top' src='" + res[i].standardImgUrl + "' alt='Card image cap' width='300px' height='200px' />" +
                        "</div>" +
                        "<div class='col-md-4' style='margin: 10px'>" +
                        "<p style='text-align: center'>Hình từ video tập theo</p>" +
                        "<img class='card-img-top' src='" + res[i].imgUrl + "'alt='Card image cap' width='300px' height='200px' />" +
                        "</div>" +
                        "</div>" +
                        "<div class='row'>" +
                        "<div class='col-md-3'></div>" +
                        "<div class='col-md-7' style='text-align: center'>" +
                        "<a class='btn btn-primary' id='" + ("btn") + res[i].id + "' href='" + ("details.html?suggestionDetailId=") + res[i].id + "'>Xem chi tiết</a>" +
                        "</div>" +
                        "</div>" +
                        "</div>";
                    $(".box-body").append(div);
                }

            }
        })
    }

})