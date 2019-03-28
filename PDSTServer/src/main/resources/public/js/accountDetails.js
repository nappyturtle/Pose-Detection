$(document).ready(function () {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var accountId = url.searchParams.get("accountId");

    currentStaff = JSON.parse(localStorage.getItem("staff"));
    if (currentStaff != undefined && currentStaff != null) {
        $("#btn-sign-out").click(function () {
            localStorage.removeItem("staff");
            window.location.href = "../login.html";
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
                        $("#detail-profile-phone").val(res.phone);
                        $("#detail-profile-email").val(res.email);
                        $("#detail-profile-address").val(res.address);

                        if (res.roleId == 3) {
                            $("#detail-profile-role").html("Trainer");
                        } else if (res.roleId == 4) {
                            $("#detail-profile-role").html("Trainee");
                        }

                        if (res.gender != null) {
                            if (res.gender.toLowerCase() == "male") {
                                $("#rdMale").iCheck('check');
                            } else if (res.gender.toLowerCase() == "female") {
                                $("#rdFemale").iCheck('check');
                            }
                        }

                        if (res.status != null) {
                            if (res.status.toLowerCase() == "active") {
                                $("#rdActive").iCheck('check');
                            } else {
                                $("#rdInActive").iCheck('check');
                            }
                        }

                        $("#btn-save-change").click(function () {
                            update(res);
                        })
                    } else {
                        console.log("null");
                    }
                }
            })

            $("#btn-profile-update").click(function () {
                $("#mUpdateInfo").html("Bạn có muốn thay đổi thông tin!");
                $("#btn-save-change").show();
            })

            function update($account) {
                var today = new Date();
                var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
                var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
                var dateTime = date + ' ' + time;
                var isMale = false;
                console.log(dateTime);
                $account.updatedTime = dateTime;
                $account.email = $("#detail-profile-email").val();
                $account.phone = $("#detail-profile-phone").val();
                $account.address = $("#detail-profile-address").val();

                var rdGender = $("input[name='r4-gender']").iCheck('update')[0].checked;
                var gender = "";
                if (rdGender) {
                    gender = "male"
                } else {
                    gender = "female";
                }
                $account.gender = gender;

                var rdStatus = $("input[name='r3-status']").iCheck('update')[0].checked;
                var status = "";
                if (rdStatus) {
                    status = "active"
                } else {
                    status = "inactive";
                }
                $account.status = status;
                console.log($account);

                $.ajax({
                    url: "/account/updateAccount",
                    type: "POST",
                    data: JSON.stringify($account),
                    headers: {
                        "content-type": "application/json; charset=UTF-8"
                    },
                    dataType: "json",
                    success: function (res) {
                        console.log(res)
                        if (res != null) {
                            if (res == true) {
                                $("#mUpdateInfo").html("Cập nhập thành công!");
                                $("#btn-save-change").hide();
                            } else {
                                $("#mUpdateInfo").html("Cập nhập thất bại!");
                                $("#btn-save-change").hide();
                            }
                        } else {
                            console.log("cập nhập thất bại");
                        }
                    }
                })
            }
        } else {
            window.location.href = "../403.html";
        }
    } else {
        window.location.href = "../403.html";
    }
})