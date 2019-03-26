$(document).ready(function () {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var accountId = url.searchParams.get("accountId");

    currentStaff = JSON.parse(localStorage.getItem("staff"));
    if (currentStaff != undefined) {
        if (currentStaff.roleId == 1 || currentStaff == 2) {
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

            //init account data
            $.ajax({
                url: "/account/update/" + accountId,
                type: "GET",
                headers: {
                    "content-type": "application/json; charset=UTF-8"
                },
                dataType: "json",
                success: function (res) {
                    if (res != null) {
                        console.log(res);
                        $("#detail-profile-user-img").attr('src', res.imgUrl);
                        $("#detail-profile-username").html(res.username);
                        if (res.roleId == 3) {
                            $("#detail-profile-role").html("Trainer");
                        } else if (res.roleId == 4) {
                            $("#detail-profile-role").html("Trainee");
                        }
                        $("#detail-profile-address").val(res.address);
                        $("#detail-profile-gender").val(res.gender);
                        $("#detail-profile-phone").val(res.phone);
                        $("#detail-profile-email").val(res.email);
                        if (res.status.toLowerCase() == "active") {
                            $("#rdActive").iCheck('check');
                        } else {
                            $("#rdInActive").iCheck('check');
                        }
                    } else {
                        console.log("null");
                    }
                }
            })
        } else {
            window.location.href = "403.html";
        }
    }
})