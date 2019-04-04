$(document).ready(function () {

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
            clickButtonUpdate();
        } else {
            window.location.href = "403.html";
        }
    } else {
        window.location.href = "403.html";
    }
})

function checkValidate() {
    var newPassword = $("#detail-profile-newPassword").val();
    var confirmPassword = $("#detail-profile-confirmPassword").val();
    var oldPassword = $("#detail-profile-oldPassword").val();
    if (newPassword == oldPassword || newPassword == null || oldPassword == null) {
        alert("Mật khẩu cũ không được trùng mật khẩu mới !!!");
        return false;
    } else if (confirmPassword !== newPassword) {
        alert("Mật khẩu mới không trùng khớp !!! ")
        return false;
    }
}

function clickButtonUpdate() {

    $("#btn-updatePassword").click(function () {
        $("#mUpdateInfo").html("Bạn có muốn thay đổi thông tin!");
        $("#btn-save-change").show();

        $("#btn-save-change").click(function () {
            if (checkValidate()) {
                changePassword();
            } else {
                alert("Kiểm tra lại mật khẩu của bạn đã nhập đúng chưa")
            }

        })

    })


}

function changePassword() {
    var formData = {
        id: currentStaff.id,
        password: $("#detail-profile-newPassword").val()
    };
    //init account data
    $.ajax({
        url: "/account/changePassword",
        type: "PUT",
        headers: {
            "content-type": "application/json; charset=UTF-8",
            "Authorization": currentStaff.token
        },
        data: JSON.stringify(formData),
        dataType: "json",
        success: function (res) {
            if (res != null) {
                $("#mUpdateInfo").html("Cập nhập thành công!");
                setTimeout(function () {
                    $('#modal-info').modal('hide');
                    $("#btn-save-change").hide();
                }, 2000);

            } else {
                $("#mUpdateInfo").html("Cập nhập thất bại!");
                $('#modal-info').modal('hide');
                $("#btn-save-change").hide();
            }
        }
    })
}

