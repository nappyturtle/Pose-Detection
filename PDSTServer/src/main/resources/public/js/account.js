var editor;

$(document).ready(function () {
    currentStaff = JSON.parse(localStorage.getItem("staff"));
    if (currentStaff != undefined && currentStaff != null) {
        if (currentStaff.roleId == 1) {
            $("#li-course-url").hide();
            $("#li-video-url").hide();
            $("#course-box-info").hide();
            $("#video-box-info").hide();
            $("#li-category-url").hide();
        }
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

            var _data;
            if (currentStaff.roleId == 1) {
                getData(2);
                $("#createNewAccount").html("Tạo tài khoản nhân viên");
                /*$("#btn-create-new-account").attr('href', "staffInfo.html");*/
            } else if (currentStaff.roleId == 2) {
                getData(3);
                $("#createNewAccount").html("Tạo tài khoản huấn luyện viên");
                /*$("#btn-create-new-account").attr('href', "../../createnewaccount.html?roleId=3");*/
            }

            $("#btn-create-account").click(function () {
                if (validData()) {
                    $("#mCreateCateMessage").html("Bạn có muốn tạo tài khoản này ?");
                    $("#btn-save-create-account").show();
                }
            })

            function validData() {
                if ($("#create-username").val().toString() == "") {
                    $("#mCreateCateMessage").html("Lỗi: Tên tài khoản không được bỏ trống!");
                    $("#btn-save-create-account").hide();
                    return false;
                } else if ($("#create-username").val().toString().indexOf(' ') !== -1) {
                    $("#mCreateCateMessage").html("Lỗi: Tên tài khoản không được có khoảng trắng!");
                    $("#btn-save-create-account").hide();
                    return false;
                } else if ($("#create-username").val().toString().length < 4) {
                    $("#mCreateCateMessage").html("Lỗi: Tên tài khoản có ít nhất 4 ký tự!");
                    $("#btn-save-create-account").hide();
                    return false;
                }

                var $result = $("#result");
                var email = $("#create-email").val();
                $result.text("");
                if ($("#create-email").val().toString() == "") {
                    $("#mCreateCateMessage").html("Lỗi: Email không được bỏ trống!");
                    $("#btn-save-create-account").hide();
                    return false;
                } else if (!validateEmail(email)) {
                    $("#mCreateCateMessage").html("Lỗi: Email không đúng định dạng!");
                    $("#btn-save-create-account").hide();
                    return false;
                }

                return true;
            }

            function validateEmail(email) {
                var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }

            $("#btn-save-create-account").click(function () {
                createNewAccount();

            })

            function createNewAccount() {
                var $account = {}
                $account.email = $("#create-email").val();
                $account.username = $("#create-username").val().toString().trim();
                if (currentStaff.roleId == 1) {
                    $account.roleId = 2;
                } else if (currentStaff.roleId == 2) {
                    $account.roleId = 3;
                }
                $account.status = "active";
                $account.prev_status = "active"
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
                                $("#mCreateCateMessage").html("Tạo tài khoản thành công!");
                                $("#btn-save-create-account").hide();
                                setTimeout(function () {
                                    location.reload();
                                }, 2000);
                            } else {
                                $("#mCreateCateMessage").html("Tạo tài khoản thất bại: Tên đăng nhật đã tồn tại!");
                                $("#btn-save-create-account").hide();
                            }
                        } else {
                            console.log("update thất bại");
                        }
                    },
                    fail: function () {
                        $("#mCreateCateMessage").html("Có lỗi xảy ra");
                        $("#btn-save-create-account").hide();
                    }
                })
            }

        } else {
            window.location.href = "../403.html";
        }
    } else {
        window.location.href = "../403.html";
    }

    function getData(roleId) {
        $.ajax({
            url: "/account/getAllAccountByRoleId?roleId=" + roleId,
            type: "GET",
            success: function (res) {
                if (roleId == 2) {
                    initDataTable(res);
                } else if (roleId == 3) {
                    $.ajax({
                        url: "/account/getAllAccountByRoleId?roleId=" + 4,
                        type: "GET",
                        success: function (resForTrainee) {
                            var data = res.concat(resForTrainee);
                            initDataTable(data);
                        },
                        fail: function () {
                        }
                    });
                }
            },
            fail: function () {
            }
        });
    }

    function initDataTable($dataSrc) {
        var $table;
        var $dataTable;
        $table = $("#tblAccount");
        $dataTable = $table.DataTable({
            paging: true,
            lengthChange: true,
            searching: true,
            ordering: true,
            info: true,
            autoWidth: true,
            data: $dataSrc,
            columnDefs: [
                {
                    searchable: false,
                    orderable: false,
                    targets: 0
                }
            ],
            columns: [
                {
                    data: null,
                    sortable: false,
                    width: 30,
                    orderable: false,
                    className: 'row-index',
                    /*render: function (index) {
                        return '<p class="stt"></p>'
                    }*/
                },
                {data: "username"},
                {data: "email"},
                {
                    data: "roleId",
                    className: 'account-role'
                },
                {
                    data: "status",
                    className: 'account-status'
                }
                ,
                {
                    data: "id",
                    searchable:
                        false,
                    orderable:
                        false,
                    render:

                        function (data, type, row) {
                            if (type === 'display') {
                                // data = '<button type="button" class="btn btn-default btn-sm btn-get-details" value="' + data + '"><i class="fa fa-fw fa-eye"></i></button>';
                                data = '<i class="fa fa-fw fa-eye" id="' + data + '" style="padding-left: 32%"></i>';
                            }
                            return data;
                        }
                }
            ],
            order: [[1, 'asc']],
            rowCallback: function (row, data, index) {
                $('.row-index', row).html(index + 1);
                var status = data.status;
                if (status != null) {
                    if (status == "active") {
                        $('.account-status', row).html("Đang hoạt động")
                            .css('color', 'green')
                            .css('font-weight', 'bold')
                            .css('text-align', 'center');
                    } else {
                        $('.account-status', row).html("Ngừng hoạt động")
                            .css('color', 'red')
                            .css('font-weight', 'bold')
                            .css('text-align', 'center');
                    }
                }
                var roleId = data.roleId;
                if (roleId == 2) {
                    $('.account-role', row).html("Nhân viên");
                } else if (roleId == 3) {
                    $('.account-role', row).html("Trainer");
                } else if (roleId == 4) {
                    $('.account-role', row).html("Trainee");
                }
            },
            buttons: [
                {extend: "edit"},
            ],
            select:
                {
                    style: 'os',
                    selector:
                        'td:first-child'
                }
        });
        //return $dataTable;
        getAccountDetails($table);
    }


    function getAccountDetails($table) {
        // $table.on('click', 'tbody .btn-get-details', function (e) {
        //     var accountId = $(this).closest('tr').find('.btn-get-details').val();
        //     window.location.href = "details.html?accountId=" + accountId;
        // })
        $table.on('click', 'tbody .fa-eye', function (e) {
            var iconElement = $(this).closest('tr').find('.fa-eye')
            var accountId = iconElement.attr('id');
            if (currentStaff.roleId == 2){
                window.location.href = "details.html?accountId=" + accountId;
            } else if(currentStaff.roleId == 1){
                window.location.href = "staffInfo.html?accountId=" + accountId;
            }

        })
    }

    /*function updateData($table, dataSrc) {
        $table.on('click', 'tbody .btn-edit', function (e) {
            var data_row = dataSrc.row($(this).parents('tr')).data();
            var status;
            var updateStatus;
            //console.log(data_row);
            var $chkbox = $(this).closest('tr').find('input[type="checkbox"]');
            if ($chkbox.length) {
                status = $chkbox.prop('checked');
                if (status) {
                    updateStatus = "active";
                } else {
                    updateStatus = "inactive";
                }
                if (data_row.status == updateStatus) {
                    $("#mUpdateInfo").html("Chưa có thông tin nào thay đổi&hellip;");
                    return;
                } else {
                    data_row.status = updateStatus;
                }
            }
            console.log(data_row);
            var today = new Date();
            var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
            var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
            var dateTime = date + ' ' + time;
            console.log(dateTime);
            data_row.updatedTime = dateTime;
            $.ajax({
                url: "/account/updateAccount",
                type: "POST",
                data: JSON.stringify(data_row),
                headers: {
                    "content-type": "application/json; charset=UTF-8"
                },
                dataType: "json",
                success: function (res) {
                    if (res != null) {
                        if (res == true) {
                            $("#mUpdateInfo").html("Cập nhập thành công!");
                        } else {
                            $("#mUpdateInfo").html("Cập nhập thất bại!");
                        }
                    } else {
                        console.log("update thật bại");
                    }
                }
            })
        })
    }*/
})

