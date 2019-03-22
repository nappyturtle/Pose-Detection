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
            if (res != null) {
                console.log(res);
                /*if (typeof (Storage) != undefined) {

                }*/
                localStorage.setItem("staff", JSON.stringify(res));
                window.location.href = "dashboard.html";
            } else {
                console.log("null");
            }
        }
    })
}