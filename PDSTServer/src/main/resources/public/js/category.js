var editor;

$(document).ready(function () {
    currentStaff = JSON.parse(localStorage.getItem("staff"));
    if (currentStaff != undefined && currentStaff != null) {
        if (currentStaff.roleId == 1) {
            $("#li-course-url").hide();
            $("#li-video-url").hide();
            $("#course-box-info").hide();
            $("#video-box-info").hide();
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
            /*if (currentStaff.roleId == 1) {
                getData(2);
                $("#createNewAccount").html("Tạo nhân viên");
                $("#btn-create-new-account").attr('href', "../../createnewaccount.html?roleId=2");
            } else if (currentStaff.roleId == 2) {
                getData(3);
                $("#createNewAccount").html("Tạo trainer");
                $("#btn-create-new-account").attr('href', "../../createnewaccount.html?roleId=3");
            }*/

            $("#createNewAccount").html("Tạo danh mục");
            $("#btn-create-new-cate").click(function () {
                $("#input-create-category-name").val("");
            })

            getData();

            $("#btn-create-categoty").click(function () {
                var cateName = $("#input-create-category-name").val().toString().trim();
                if (cateName == "") {
                    $("#mCreateCateMessage").html("Lỗi: Tên danh mục không được bỏ trống!");
                    $("#btn-save-create-cate").hide();
                } else {
                    $("#mCreateCateMessage").html("Bạn có muốn tạo danh mục mới ?");
                    $("#btn-save-create-cate").show();
                }
            })

            $("#btn-save-create-cate").click(function () {
                var newCate = {};
                var cateName = $("#input-create-category-name").val().toString().trim();
                console.log(cateName);
                newCate.name = cateName;
                newCate.status = "active";
                var today = new Date();
                var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
                var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
                var dateTime = date + ' ' + time;
                newCate.createdTime = dateTime;
                $.ajax({
                    url: "/category/create",
                    type: "POST",
                    data: JSON.stringify(newCate),
                    headers: {
                        "content-type": "application/json; charset=UTF-8",
                        "Authorization": currentStaff.token
                    },
                    dataType: "json",
                    success: function (resMess) {
                        console.log(resMess);
                        if (resMess.message == "existed") {
                            $("#mCreateCateMessage").html("Lỗi: Tên danh mục đã tồn tại!");
                            $("#btn-save-create-cate").hide();
                        } else if (resMess.message == "success") {
                            $("#mCreateCateMessage").html("Cập nhật thành công");
                            $("#btn-save-create-cate").hide();
                            setTimeout(function () {
                                location.reload()
                            }, 1000);
                        } else if (resMess.message == "fail") {
                            $("#mCreateCateMessage").html("Đã có lỗi xảy ra!");
                            $("#btn-save-create-cate").hide();
                        }
                    }
                })
            })

        } else {
            window.location.href = "../403.html";
        }
    } else {
        window.location.href = "../403.html";
    }

    function getData() {
        $.ajax({
            url: "/category/categories",
            type: "GET",
            dataType: "json",
            success: function (res) {
                initDataTable(res)
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
                {data: "name"},
                {
                    data: "status",
                    className: 'account-status',
                    searchable: false,
                    orderable: false,
                }
                ,
                {
                    data: "id",
                    searchable: false,
                    orderable: false,
                    width: "50px",
                    render: function (data, type, row) {
                        if (type === 'display') {
                            // data = '<button type="button" class="btn btn-default btn-sm btn-get-details" value="' + data + '"><i class="fa fa-fw fa-eye"></i></button>';
                            data = '<i class="fa fa-fw fa-edit" data-toggle="modal" data-target="#modal-update-category" id="' + data + '" style="padding-left: 32%"></i>';
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
                /*var roleId = data.roleId;
                if (roleId == 2) {
                    $('.account-role', row).html("Nhân viên");
                } else if (roleId == 3) {
                    $('.account-role', row).html("Trainer");
                } else if (roleId == 4) {
                    $('.account-role', row).html("Trainee");
                }*/
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
        getCategoryDetails($table);
    }

    function getCategoryDetails($table) {
        $table.on('click', 'tbody .fa-edit', function (e) {
            var iconElement = $(this).closest('tr').find('.fa-edit')
            var cateId = iconElement.attr('id');
            $.ajax({
                url: "/category/" + cateId,
                type: "GET",
                headers: {
                    "Authorization": currentStaff.token
                },
                dataType: "json",
                success: function (res) {
                    console.log(res);
                    $("#input-category-name").val(res.name);
                    if (res.status != null) {
                        if (res.status == "active") {
                            $("#select-category-status").val("active");
                        } else if (res.status == "inactive") {
                            $("#select-category-status").val("inactive");
                        }
                    }

                    $("#btn-save-categoty-details").click(function () {
                        var cateName = $("#input-category-name").val().toString().trim();
                        console.log("cate name " + cateName);
                        if (cateName == "") {
                            $("#mUpdateInfo").html("Lỗi: Tên danh mục không được bỏ trống");
                            $("#btn-save-change").hide();
                        } else {
                            $("#mUpdateInfo").html("Bạn có muốn cập nhật thông tin ?");
                            $("#btn-save-change").show();
                        }
                    })

                    $("#btn-save-change").click(function () {
                        var cateName = $("#input-category-name").val().toString().trim();
                        res.name = cateName;
                        res.status = $("#select-category-status option:selected").val();
                        var today = new Date();
                        var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
                        var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
                        var dateTime = date + ' ' + time;
                        res.updatedTime = dateTime;
                        console.log(res);
                        $.ajax({
                            url: "/category/update",
                            type: "POST",
                            data: JSON.stringify(res),
                            headers: {
                                "content-type": "application/json; charset=UTF-8",
                                "Authorization": currentStaff.token
                            },
                            dataType: "json",
                            success: function (responseMessage) {
                                console.log(responseMessage)
                                if (responseMessage.message == "existed") {
                                    $("#mUpdateInfo").html("Lỗi: Tên danh mục đã tồn tại!");
                                    $("#btn-save-change").hide();
                                } else if (responseMessage.message == "success") {
                                    $("#mUpdateInfo").html("Cập nhật thành công");
                                    $("#btn-save-change").hide();
                                    setTimeout(function () {
                                        location.reload()
                                    }, 1000);
                                } else if (responseMessage.message == "fail") {
                                    $("#mUpdateInfo").html("Đã có lỗi xảy ra!");
                                    $("#btn-save-change").hide();
                                }
                            }
                        })
                    })
                }
            })
        })
    }

    function editCate(category) {

    }

    function validData() {
        var cateName = $("#input-category-name").val().toString().trim();
        console.log("cate name " + cateName);
        if (cateName == "") {
            $("#mUpdateInfo").html("Lỗi: Tên danh mục không được bỏ trống");
            $("#btn-save-change").hide();
            return false;
        }
        return true;
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

