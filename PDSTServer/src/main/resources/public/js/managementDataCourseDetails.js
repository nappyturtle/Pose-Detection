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
            // $("#pull-lelf-user-img").attr('src', currentStaff.imgUrl);
            // $("#pull-lelf-username").html(currentStaff.username);
            $("#dropdown-menu-imgUrl").attr('src', currentStaff.imgUrl);
            $("#dropdown-menu-username").html(currentStaff.username);
            if (currentStaff.roleId == 1) {
                $("#dropdown-menu-role").html("Admin");
            } else {
                $("#dropdown-menu-role").html("Staff");
            }
            //init course data
            initCourseDetail(parseInt(courseId, 10));

            clickButtonUpdate(parseInt(courseId, 10));
        } else {
            window.location.href = "403.html";
        }
    } else {
        window.location.href = "403.html";
    }

})
function clickButtonUpdate(courseId) {
    $("#btn-course-update").click(function () {
        $("#mUpdateInfo").html("Bạn có muốn thay đổi thông tin!");
        $("#btn-save-change").show();
    })
    $("#btn-save-change").click(function () {
        editCourseDetail(courseId)
    })
}
function editCourseDetail(courseId) {

    console.log('da vao edit course nay ne ');

    // $('form').submit(function (event) {
    //     event.preventDefault();
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
            /*
            coursename: $("#detail-course-name").val(),
            categoryId: parseInt($('#slt-category option:selected').val(), 10),
            price: parseInt($("#detail-course-price").val(), 10),
            */
            status: status
        };

            $.ajax({
                url: "/course/editCourseByStaffOrAdmin",
                type: "PUT",
                data: JSON.stringify(formData),
                headers: {
                    "content-type": "application/json; charset=UTF-8",
                    "Authorization": currentStaff.token
                },
                dataType: "json",

            }).done(function (res) {
                console.log(res);
                // if (res != null) {
                //     if (res == true) {
                //         alert("Cập nhập thành công!");
                //         location.reload();
                //     } else {
                //         alert("Cập nhập thất bại!")
                //     }
                // } else {
                //     console.log("update thật bại");
                // }

                if (res != null) {
                    if (res == true) {
                        $("#mUpdateInfo").html("Cập nhập thành công!");
                        setTimeout(function () {
                            $('#modal-info').modal('hide');
                            $("#btn-save-change").hide();
                            location.reload();
                        },2000);

                    } else {
                        $("#mUpdateInfo").html("Cập nhật thất bại!");
                        setTimeout(function () {
                            $('#modal-info').modal('hide');
                            $("#btn-save-change").hide();
                            location.reload();
                        },2000);

                    }
                } else {
                    console.log("cập nhập thất bại");
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

                // Convert to String and add dots every 3 digits
                var moneyDots = res.price.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1.");

                $("#detail-course-price").val(moneyDots+".000");
                $("#detail-course-category").val(res.categoryname);
                if (res.status.toLowerCase() == "active") {
                    //$("#rdActive").prop("checked", true);
                    $("#rdActive").iCheck('check');
                    $("#rdActive").val(res.status);
                    $("#rdInActive").val("inactive");
                } else {
                    //$("#rdInActive").prop("checked", true);
                    $("#rdInActive").iCheck('check');
                    $("#rdInActive").val(res.status);
                    $("#rdActive").val("active");
                }

                /*
                $('#slt-category').html('');
                //iterate over the data and append a select option
                // $('#slt-category').append('<option value="0">Chọn danh mục...</option>');
                $.each(res.categoryList, function (key, val) {
                    $('#slt-category').append('<option value="' + val.id + '"' + (val.id == res.categoryId ? ' selected' : '') + '>' + val.name + '</option>');
                })
                */
            } else {
                console.log("null");
            }
        }
    })
}

