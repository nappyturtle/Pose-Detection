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
                $("#dropdown-menu-role").html("Nhân viên");
            }
            //init course data
            initCourseDetail(parseInt(courseId, 10));

            //staff's fullname
            $.ajax({
                url: "/account/update/" + currentStaff.id,
                type: "GET",
                headers: {
                    "content-type": "application/json; charset=UTF-8"
                },
                dataType: "json",
                success: function (res) {
                    if (res.fullname != null) {
                        $(".navbar-username").html(res.fullname);
                    } else {
                        $(".navbar-username").html(res.username);
                    }
                }
            })
            initVideoTable(courseId);
            $("#btn-goto-video-in-course").click(function () {
                window.location.href = "../../management/video/layout.html?courseId=" + courseId;
            })
        } else {
            window.location.href = "403.html";
        }
    } else {
        window.location.href = "403.html";
    }

})

function initVideoTable(courseId) {
    var dataSrc;
    $.ajax({
        url: "/video/getVideoByCourseId/" + courseId,
        type: "GET",
        headers: {
            "Authorization": currentStaff.token
        },
        success: function (res) {
            console.log(res)
            dataSrc = $('#tblVideo').DataTable({
                paging: true,
                lengthChange: false,
                pageLength: 5,
                searching: true,
                ordering: true,
                info: true,
                //autoWidth: true,
                data: res
                ,
                columns: [
                    {
                        data: null,
                        sortable: true,
                        width: 30,
                        orderable: true,
                        className: 'row-index'
                    },
                    {data: "title"},
                    {
                        data: "id",
                        searchable: false,
                        orderable: false,
                        className: "text-center",
                        render: function (data, type, row) {
                            if (type === 'display') {
                                data = '<i class="fa fa-fw fa-eye" id="' + data + '"></i>';
                            }
                            return data;
                        }
                    }
                ],
                rowCallback: function (row, data, index) {
                    $('.row-index', row).html(index + 1);
                },
                buttons: [
                    {extend: "edit"},
                ],
                select: {
                    style: 'os',
                    selector: 'td:first-child'
                }
            });
            getCourseDtail($('#tblVideo'));
        }
    })
}

function getCourseDtail($table) {
    $table.on('click', 'tbody .fa-eye', function (e) {
        var iconElement = $(this).closest('tr').find('.fa-eye')
        var videoId = iconElement.attr('id');
        window.location.href = "../../management/video/details.html?videoId=" + videoId;
    })
}

function editCourseDetail(course) {

    $.ajax({
        url: "/course/editCourseByStaffOrAdmin",
        type: "PUT",
        data: JSON.stringify(course),
        headers: {
            "content-type": "application/json; charset=UTF-8",
            "Authorization": currentStaff.token
        },
        dataType: "json",

    }).done(function (res) {
        if (res != null) {
            if (res == true) {
                $("#mUpdateInfo").html("Cập nhật thành công!");
                $("#btn-save-change").hide();
                setTimeout(function () {
                    window.location.reload()
                }, 2000);

            } else {
                $("#mUpdateInfo").html("Cập nhập thất bại!");
                $("#btn-save-change").hide();
                setTimeout(function () {
                    window.location.reload()
                }, 2000);            }
        } else {
            console.log("cập nhật thất bại");
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
                var cateId = res.categoryId;
                $("#detail-course-thumbnailImg").attr('src', res.thumbnail);
                $("#detail-course-name").html(res.coursename);
                $("#detail-course-trainer").html(res.accountname);

                // Convert to String and add dots every 3 digits
                var moneyDots = res.price.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1.");

                $("#detail-course-price").html(moneyDots + ".000");
                $("#detail-course-category").html(res.categoryname);
                if (res.status != null) {
                    if (res.status.toLowerCase() == "active") {
                        //$("#rdActive").iCheck('check');
                        $("#detail-course-status").html("Đang hoạt động").css('color', 'green');
                    } else {
                        //$("#rdInActive").iCheck('check');
                        $("#detail-course-status").html("Ngưng hoạt động").css('color', 'red');
                    }
                }

                $("#btn-profile-change-info").click(function () {
                    $(".change-info").show();
                    $(".init-info").hide();
                    $("#btn-profile-change-info").hide();

                    $("#input-detail-course-name").val(res.coursename);
                    $("#input-detail-course-price").val(res.price);
                    $("#div-detail-course-status").css('display', 'inline-block');
                    if (res.status != null) {
                        if (res.status.toLowerCase() == "active") {
                            $("#select-detail-course-status").val("active");
                        } else {
                            //$("#rdInActive").iCheck('check');
                            $("#select-detail-course-status").val("inactive");
                        }
                    }

                    var courseEdit = res;
                    $.ajax({
                        url: "/category/getActiveCategory",
                        type: "GET",
                        headers: {
                            "Authorization": currentStaff.token,
                            "Content-Type": "application/json"
                        },
                        dataType: "json",
                        success: function (res) {
                            console.log(res)
                            // Get select
                            var select = document.getElementById('select-detail-course-category');

                            $(select).find('option')
                                .remove()
                                .end()
                            $(select).val("");
                            // Add options
                            for (var i in res) {
                                $(select).append('<option style="width: 100%;" value=' + res[i].id + '>' + res[i].name + '</option>');
                                console.log("cateId " + cateId);

                                if (cateId == null) {
                                    $(select).val("");
                                } else if (res[i].id == cateId) {
                                    console.log("res cateid " + res[i].id);
                                    //$("#select-detail-course-category").val("2");
                                    $(select).val(cateId);
                                }
                            }


                            $("#btn-save-change").click(function () {
                                courseEdit.categoryId = $("#select-detail-course-category").val();
                                courseEdit.coursename = $("#input-detail-course-name").val();
                                courseEdit.price = $("#input-detail-course-price").val();
                                courseEdit.status = $("#select-detail-course-status").val();
                                console.log(courseEdit);
                                editCourseDetail(courseEdit)
                            })
                        }
                    })
                });

                $("#btn-profile-cancel-update").click(function () {
                    $(".change-info").hide();
                    $(".init-info").show();
                })

                $("#btn-profile-update").click(function () {
                    if (validData()) {
                        $("#mUpdateInfo").html("Bạn có muốn thay đổi thông tin ?");
                        $("#btn-save-change").show();
                    }
                })

                function validData() {

                    if ($("#input-detail-course-name").val().toString().trim() == "") {
                        $("#mUpdateInfo").html("Lỗi: Tên khóa học không được bỏ trống!");
                        $("#btn-save-change").hide();
                        return false;
                    }

                    if ($("#input-detail-course-price").val().toString().trim() == "") {
                        $("#mUpdateInfo").html("Lỗi: Giá tiền không được bỏ trống!");
                        $("#btn-save-change").hide();
                        return false;
                    }

                    var price = $("#input-detail-course-price").val().toString().trim();
                    var isnum = /^\d+$/.test(price);
                    //console.log(isnum + " = isnum");
                    if (!isnum) {
                        $("#mUpdateInfo").html("Lỗi: Giá tiền không đúng định dạng!");
                        $("#btn-save-change").hide();
                        return false;
                    }

                    return true;
                }

            } else {
                console.log("null");
            }
        }
    })
}

