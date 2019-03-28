$(document).ready(function () {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var courseId = url.searchParams.get("courseId");
    console.log('typeOF courseId = ' + typeof courseId);
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
            initCourseDetail(parseInt(courseId, 10));

            editCourseDetail(parseInt(courseId, 10), currentStaff.token);
        } else {
            window.location.href = "403.html";
        }
    }else {
        window.location.href = "403.html";
    }

})

function editCourseDetail(courseId, token) {

    console.log('da vao edit course nay ne ');

    $('form').submit(function (event) {
        event.preventDefault();
        console.log("You pressed OK!");
        var radioValue = $("input[name='r3-status']").iCheck('update')[0].checked;

        var status = "";
        if (radioValue) {
            status = "active"
        } else {
            status = "inactive";
        }
        var formData = {
            id: courseId,
            coursename: $("#detail-course-name").val(),
            categoryId: parseInt($('#slt-category option:selected').val(), 10),
            price: parseInt($("#detail-course-price").val(), 10),
            status: status
        };
        //alert("Your are a - " + courseId + "-" + coursename + "-" + categoryId + "-" + price + "-" + status);
        var r = confirm("Bạn có muốn thay đổi thông tin không?");
        if (r == true) {
            $.ajax({
                url: "/course/editCourseByStaffOrAdmin",
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

function initCourseDetail(courseId) {
    $.ajax({
        url: "/course/getCourseDetailById/" + courseId,
        type: "GET",
        headers: {
            "content-type": "application/json; charset=UTF-8",
            "Authorization": currentStaff.token
        },
        dataType: "json",
        success: function (res) {
            if (res != null) {
                console.log(res);
                $("#detail-course-thumbnailImg").attr('src', res.thumbnail);
                $("#detail-course-name").val(res.coursename);
                $("#detail-course-trainer").val(res.accountname);
                $("#detail-course-price").val(res.price);
                if (res.status.toLowerCase() == "active") {
                    //$("#rdActive").prop("checked", true);
                    $("#rdActive").iCheck('check');
                    $("#rdActive").val(res.status);
                } else {
                    //$("#rdInActive").prop("checked", true);
                    $("#rdInActive").iCheck('check');
                    $("#rdInActive").val(res.status);
                }

                $('#slt-category').html('');
                //iterate over the data and append a select option
                // $('#slt-category').append('<option value="0">Chọn danh mục...</option>');
                $.each(res.categoryList, function (key, val) {
                    $('#slt-category').append('<option value="' + val.id + '"' + (val.id == res.categoryId ? ' selected' : '') + '>' + val.name + '</option>');
                })
            } else {
                console.log("null");
            }
        }
    })
}

