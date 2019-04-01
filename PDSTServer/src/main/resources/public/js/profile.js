$(document).ready(function () {

    currentStaff = JSON.parse(localStorage.getItem("staff"));
    if (currentStaff != undefined && currentStaff != null) {
        $("#btn-sign-out").click(function () {
            localStorage.removeItem("staff");
            window.location.href = "../../login.html";
        })
        if (currentStaff.roleId == 1) {
            $("#li-course-url").hide();
            $("#li-video-url").hide();
            $("#course-box-info").hide();
            $("#video-box-info").hide();
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

            console.log(currentStaff);
            //init account data
            $.ajax({
                url: "/account/update/" + currentStaff.id,
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
                        } else if (res.roleId == 2) {
                            $("#detail-profile-role").html("Nhân viên");
                        } else if (res.roleId == 1) {
                            $("#detail-profile-role").html("Admin");
                        }

                        if (res.gender != null) {
                            if (res.gender.toLowerCase() == "male") {
                                $("#rdMale").iCheck('check');
                                /*$("#detail-profile-gender").val("Nam");*/
                            } else if (res.gender.toLowerCase() == "female") {
                                $("#rdFemale").iCheck('check');
                                /*$("#detail-profile-gender").val("Nữ")*/
                            }
                        }

                        if (res.status != null) {
                            if (res.status.toLowerCase() == "active") {
                                /*$("#rdActive").iCheck('check');*/
                                $("#detail-profile-status").val("Đang hoạt động").css('color', 'green');
                            } else {
                                /*$("#rdInActive").iCheck('check');*/
                                $("#detail-profile-status").val("Ngừng hoạt động").css('color', 'red');
                            }
                        }

                        $("#btn-save-change").click(function () {
                            update(res);
                        })

                        $("#btn-profile-update").click(function () {
                            if (validData()) {
                                $("#mUpdateInfo").html("Bạn có muốn cập nhật thông tin không ?");
                                $("#btn-save-change").show();
                            }
                        })
                    } else {
                        console.log("null");
                    }
                }
            })

            function validData() {
                var email = $("#detail-profile-email").val();
                if ($("#detail-profile-email").val().toString() == "") {
                    $("#mUpdateInfo").html("Lỗi: Email không được bỏ trống!");
                    $("#btn-save-change").hide();
                    return false;
                } else if (!validateEmail(email)) {
                    $("#mUpdateInfo").html("Lỗi: Email không đúng định dạng!");
                    $("#btn-save-change").hide();
                    return false;
                }

                return true;
            }

            function validateEmail(email) {
                var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }

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

                /*var rdStatus = $("input[name='r3-status']").iCheck('update')[0].checked;
                var status = "";
                if (rdStatus) {
                    status = "active"
                } else {
                    status = "inactive";
                }
                $account.status = status;
                console.log($account);*/

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
                                $("#mUpdateInfo").html("Cập nhật thành công!");
                                $("#btn-save-change").hide();
                                //dismissDialog();
                            } else {
                                $("#mUpdateInfo").html("Cập nhật thất bại!");
                                $("#btn-save-change").hide();
                                //dismissDialog();
                            }
                        } else {
                            console.log("cập nhật thất bại");
                        }
                    }
                })
            }

            function dismissDialog() {
                setTimeout($('#modal-default').hide(), 4000);
            }
        } else {
            window.location.href = "../403.html";
        }
    } else {
        window.location.href = "../403.html";
    }
})