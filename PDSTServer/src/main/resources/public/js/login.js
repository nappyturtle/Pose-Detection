function signin() {
    var username = $("#username").val();
    var password = $("#password").val();
    var _data = {
        username: username,
        password: password
    }

    $.ajax({
        url: "/login",
        type: "POST",
        data: JSON.stringify(_data),
        headers: {
            "content-type": "application/json; charset=UTF-8"
        },
        dataType: "json",
        success: function (res) {
            console.log(res)
            if (res != null) {
                if (res.roleId == 1 || res.roleId == 2) {
                    console.log(res.roleId)
                    if (typeof (Storage) != undefined) {
                        localStorage.setItem('staff', JSON.stringify(res));
                    }
                    window.location.href = "management/account/account.html";
                } else {
                    window.location.href = "403.html";
                }

            } else {
                $("#errorMess").show();
            }
        },
        fail: function () {
            $("#errorMess").show();
        }
    })
}