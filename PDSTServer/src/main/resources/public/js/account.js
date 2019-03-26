var editor;

$(document).ready(function () {
    currentStaff = JSON.parse(localStorage.getItem("staff"));
    if (currentStaff != undefined) {
        if (currentStaff.roleId == 1 || currentStaff == 2) {
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

            initDataTable(3);

            initDataTable(4);

        } else {
            window.location.href = "403.html";
        }
    }

    function initDataTable(roleId) {
        var $table;
        var dataSrc
        if (roleId == 3) {
            $table = $("#tblTrainer");
        } else if (roleId == 4) {
            $table = $("#tblTrainee");
        }
        $.ajax({
            url: "/account/getAllAccountByRoleId?roleId=" + roleId,
            type: "GET",
            success: function (res) {
                console.log(res)
                dataSrc = $table.DataTable({
                    paging: true,
                    lengthChange: true,
                    searching: true,
                    ordering: true,
                    info: true,
                    //autoWidth: true,
                    data: res,
                    columnDefs: [
                        {
                            targets: 1,
                            searchable: false,
                            orderable: false,
                            render: function (data, type, row) {
                                if (type === 'display') {
                                    data = '<img style="width: 50px; height: 50px" src="' + data + ' "/>';
                                }
                                return data;
                            }
                        }, {
                            targets: 4,
                            searchable: false,
                            orderable: false,
                            className: 'dt-body-center',
                            render: function (data, type, full, meta) {
                                return '<input type="checkbox" class="editor-active dt-body-center">';
                            }
                        }
                    ],
                    columns: [
                        {
                            data: null,
                            "sortable": false,
                            render: function () {
                                return '<p></p>'
                            }
                        },
                        {data: "imgUrl"},
                        {data: "username"},
                        {data: "email"},
                        {data: "status"},
                        {
                            data: "id",
                            searchable: false,
                            orderable: false,
                            render: function (data, type, row) {
                                if (type === 'display') {
                                    data = '<button type="button" class="btn btn-info btn-get-details" value="' + data + '">Xem chi tiết</button>';
                                }
                                return data;
                            }
                        }
                    ],
                    rowCallback: function (row, data) {
                        // Set the checked state of the checkbox in the table
                        $('input.editor-active', row).prop('checked', data.status == "active");
                    },
                    buttons: [
                        {extend: "edit"},
                    ],
                    select: {
                        style: 'os',
                        selector: 'td:first-child'
                    }
                });
                //updateData($table, dataSrc);
                getAccountDetails($table, dataSrc);
            }
        })
    }


    function getAccountDetails($table, dataSrc) {
        $table.on('click', 'tbody .btn-get-details', function (e) {
            var accountId = $(this).closest('tr').find('.btn-get-details').val();
            window.location.href = "details.html?accountId=" + accountId;
        })
    }

    function updateData($table, dataSrc) {
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
    }
})

