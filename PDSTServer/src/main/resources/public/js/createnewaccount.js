$(document).ready(function () {
    currentStaff = JSON.parse(localStorage.getItem("staff"));
    if (currentStaff != undefined && currentStaff != null) {
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

            //init account data
            var url_string = window.location.href;
            var newAccountRoleId = url_string.split('=').pop();
            console.log(newAccountRoleId + " = role");

            if (newAccountRoleId == 3) {
                $("#breadcrumnb-role").html("trainer");
                $("#menu-create-trainer").addClass("active");
                $("#admin-authorized").removeClass("active");
            } else if (newAccountRoleId == 2) {
                $("#breadcrumnb-role").html("nhân viên");
                $("#menu-create-trainer").removeClass("active");
                $("#admin-authorized").addClass("active");
            }

            $("#btn-create-new-account").click(function () {
                if (validData()) {
                    $("#mUpdateInfo").html("Bạn có muốn thay đổi thông tin!");
                    $("#btn-save-change").show();
                }
            })

            function validData() {
                if ($("#create-username").val().toString().trim() == "") {
                    $("#mUpdateInfo").html("Lỗi: Tên tài khoản không được bỏ trống!");
                    $("#btn-save-change").hide();
                    return false;
                }
                if ($("#create-email").val().toString().trim() == "") {
                    $("#mUpdateInfo").html("Lỗi: Email không được bỏ trống!");
                    $("#btn-save-change").hide();
                    return false;
                }
                if ($("#create-password").val().toString().trim() == "") {
                    $("#mUpdateInfo").html("Lỗi: Mật khẩu không được bỏ trống!");
                    $("#btn-save-change").hide();
                    return false;
                }
                if ($("#create-retype-password").val().toString().trim() !== $("#create-password").val().toString().trim()) {
                    $("#mUpdateInfo").html("Lỗi: Mật khẩu xác nhận không đúng!");
                    $("#btn-save-change").hide();
                    return false;
                }
                return true;
            }


            $("#btn-save-change").click(function () {
                createNewAccount()
            })

            function createNewAccount() {
                var $account = {}
                $account.email = $("#create-email").val();
                $account.phone = $("#create-phone").val();
                $account.username = $("#create-username").val().toString().trim();
                $account.password = $("#create-password").val().toString().trim();
                $account.roleId = newAccountRoleId;
                $account.status = "active";
                console.log($account);

                $.ajax({
                    url: "/account/createNewAccount",
                    type: "POST",
                    data: JSON.stringify($account),
                    headers: {
                        "content-type": "application/json; charset=UTF-8"
                    },
                    dataType: "json",
                    success: function (res) {
                        console.log(res)
                        if (res != null) {
                            if (res.message == "success") {
                                $("#mUpdateInfo").html("Tạo tài khoản thành công!");
                                $("#btn-save-change").hide();
                            } else {
                                $("#mUpdateInfo").html("Tạo tài khoản thất bại!");
                                $("#btn-save-change").hide();
                            }
                        } else {
                            console.log("update thất bại");
                        }
                    },
                    fail: function () {
                        $("#mUpdateInfo").html("Có lỗi xảy ra");
                        $("#btn-save-change").hide();
                    }
                })
            }

            $("#btn-sign-out").click(function () {
                localStorage.removeItem("staff");
                window.location.href = "../login.html";
            })
        } else {
            window.location.href = "../403.html";
        }
    } else {
        window.location.href = "../403.html";
    }
})