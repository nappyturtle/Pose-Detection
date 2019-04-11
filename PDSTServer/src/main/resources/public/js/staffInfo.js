$(document).ready(function () {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var accountId = url.searchParams.get("accountId");

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
            $("#li-category-url").hide();
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
                $("#dropdown-menu-role").html("Nhân viên");
            }
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
                        $("#detail-profile-phone").html(res.phone);
                        $("#detail-profile-email").html(res.email);
                        $("#detail-profile-address").html(res.address);

                        if (res.fullname != null) {
                            $("#detail-profile-fullname").html(res.fullname);
                        } else {
                            $("#detail-profile-fullname").html(res.username);
                        }

                        if (res.roleId == 3) {
                            $("#detail-profile-role").html("Trainer");
                        } else if (res.roleId == 4) {
                            $("#detail-profile-role").html("Trainee");
                        } else if (res.roleId == 2) {
                            $("#detail-profile-role").html("Nhân viên");
                        }

                        if (res.gender != null) {
                            if (res.gender.toLowerCase() == "male") {
                                /*$("#rdMale").iCheck('check');*/
                                $("#detail-profile-gender").html("Nam");
                            } else if (res.gender.toLowerCase() == "female") {
                                /*$("#rdFemale").iCheck('check');*/
                                $("#detail-profile-gender").html("Nữ")
                            }
                        }

                        if (res.status != null) {
                            if (res.status.toLowerCase() == "active") {
                                //$("#rdActive").iCheck('check');
                                $("#detail-profile-status").html("Đang hoạt động").css('color', 'green');
                            } else {
                                //$("#rdInActive").iCheck('check');
                                $("#detail-profile-status").html("Ngưng hoạt động").css('color', 'red');
                            }
                        }

                        $("#btn-profile-change-info").click(function () {
                            $(".change-info").show();
                            $(".init-info").hide();
                            $("#input-detail-profile-address").val(res.address);
                            $("#input-detail-profile-email").val(res.email);
                            $("#input-detail-profile-phone").val(res.phone);
                            $("#div-detail-profile-gender").css('display', 'inline-block');
                            if (res.gender != null) {
                                if (res.gender.toLowerCase() == "male") {
                                    $("#select-detail-profile-gender").val("male");
                                } else if (res.gender.toLowerCase() == "female") {
                                    $("#select-detail-profile-gender").val("female");
                                }
                            }

                            $("#div-detail-profile-status").css('display', 'inline-block');
                            if (res.status != null) {
                                if (res.status.toLowerCase() == "active") {
                                    $("#select-detail-profile-status").val("active");
                                } else {
                                    //$("#rdInActive").iCheck('check');
                                    $("#select-detail-profile-status").val("inactive");
                                }
                            }

                            $("#btn-profile-change-info").hide();

                        });

                        $("#btn-save-change").click(function () {
                            update(res);
                        })

                        $("#btn-profile-update").click(function () {
                            if (validData()) {
                                $("#mUpdateInfo").html("Bạn có muốn thay đổi thông tin ?");
                                $("#btn-save-change").show();
                            }
                        })

                        $("#btn-profile-cancel-update").click(function () {
                            $(".change-info").hide();
                            $(".init-info").show();
                        })

                        if(res.roleId == 4){
                            $("#div-tbl-created-course").hide();
                        }
                        /*initCreatedCourseTable(res.id);
                        initBoughtCourseTable(res.id);*/
                    } else {
                        console.log("null");
                    }
                }
            })

            function update($account) {
                var today = new Date();
                var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
                var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
                var dateTime = date + ' ' + time;
                var isMale = false;
                console.log(dateTime);
                $account.updatedTime = dateTime;
                $account.email = $("#input-detail-profile-email").val();
                $account.phone = $("#input-detail-profile-phone").val();
                $account.address = $("#input-detail-profile-address").val();
                $account.status = $("#select-detail-profile-status option:selected").val();
                $account.gender = $("#select-detail-profile-gender option:selected").val();

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
                                // $("#btn-save-change").hide();
                                setTimeout(function () {
                                    location.reload();
                                }, 1000);
                                //dismissDialog();
                                //VuVG 2/4/2019
                                setTimeout(function () {
                                    $('#modal-info').modal('hide');
                                    $("#btn-save-change").hide();
                                    location.reload();
                                }, 2000);
                            } else {
                                $("#mUpdateInfo").html("Cập nhật thất bại!");
                                // $("#btn-save-change").hide();
                                //dismissDialog();
                                //VuVG 2/4/2019
                                setTimeout(function () {
                                    $('#modal-info').modal('hide');
                                    $("#btn-save-change").hide();
                                    location.reload();
                                }, 2000);
                            }
                        } else {
                            console.log("cập nhật thất bại");
                        }
                    }
                })
            }

            function validData() {
                console.log($("#input-detail-profile-phone").val().toString().trim().length)
                var email = $("#input-detail-profile-email").val();
                if ($("#input-detail-profile-email").val().toString() == "") {
                    $("#mUpdateInfo").html("Lỗi: Email không được bỏ trống!");
                    $("#btn-save-change").hide();
                    return false;
                } else if (!validateEmail(email)) {
                    $("#mUpdateInfo").html("Lỗi: Email không đúng định dạng!");
                    $("#btn-save-change").hide();
                    return false;
                }

                if ($("#input-detail-profile-phone").val() == "") {
                    $("#mUpdateInfo").html("Lỗi: Số diện thoại không được bỏ trống!");
                    $("#btn-save-change").hide();
                    return false;
                } else if ($("#input-detail-profile-phone").val().toString().trim().length < 10 || $("#input-detail-profile-phone").val().toString().trim().length > 15) {
                    $("#mUpdateInfo").html("Lỗi: Số diện thoại không đúng dịnh dạng!");
                    $("#btn-save-change").hide();
                    return false;
                }
                return true;
            }

            function validateEmail(email) {
                var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }

        } else {
            window.location.href = "../403.html";
        }
    } else {
        window.location.href = "../403.html";
    }

    function initCreatedCourseTable(accountId) {
        var dataSrc;
        $.ajax({
            url: "/course/getAllCoursesByAccountId?accountId=" + accountId,
            type: "GET",

            headers: {
                "Authorization": currentStaff.token,
                "Content-Type": "application/json"
            },
            success: function (res) {
                console.log(res)
                dataSrc = $('#tblCreatedCourse').DataTable({
                    pageLength: 5,
                    paging: true,
                    lengthChange: false,
                    searching: true,
                    ordering: true,
                    info: true,
                    //autoWidth: true,
                    data: res
                    ,
                    columns: [

                        {
                            data: null,
                            sortable: false,
                            width: 30,
                            orderable: false,
                            className: 'row-index'
                        },
                        {data: "name"},
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
                    ]
                    ,
                    buttons: [
                        {extend: "edit"},
                    ],
                    select: {
                        style: 'os',
                        selector: 'td:first-child'
                    },
                    rowCallback: function (row, data, index) {
                        $('.row-index', row).html(index + 1);
                    }
                });
                getCourseDtail($('#tblCreatedCourse'));
            }
        })
    }

    function initBoughtCourseTable(accountId) {
        var dataSrc;
        $.ajax({
            url: "/enrollment/getBoughtCourseByAccountId/" + accountId,
            type: "GET",

            headers: {
                "Authorization": currentStaff.token,
                "Content-Type": "application/json"
            },
            success: function (res) {
                console.log(res)
                dataSrc = $('#tblBoughtCourse').DataTable({
                    pageLength: 5,
                    paging: true,
                    lengthChange: false,
                    searching: true,
                    ordering: true,
                    info: true,
                    //autoWidth: true,
                    data: res
                    ,
                    columns: [

                        {
                            data: null,
                            sortable: false,
                            width: 30,
                            orderable: false,
                            className: 'row-index'
                        },
                        {data: "name"},
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
                    ]
                    ,
                    buttons: [
                        {extend: "edit"},
                    ],
                    select: {
                        style: 'os',
                        selector: 'td:first-child'
                    },
                    rowCallback: function (row, data, index) {
                        $('.row-index', row).html(index + 1);
                    }
                });
                getCourseDtail($('#tblBoughtCourse'));
            }
        })
    }

    function getCourseDtail($table) {
        $table.on('click', 'tbody .fa-eye', function (e) {
            var iconElement = $(this).closest('tr').find('.fa-eye')
            var courseId = iconElement.attr('id');
            window.location.href = "../../management/course/details.html?courseId=" + courseId;
        })
    }

})