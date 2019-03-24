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

            var roleId = 3;
            var dataSrc;
            $.ajax({
                url: "/account/getAllAccountByRoleId?roleId=" + roleId,
                type: "GET",
                success: function (res) {
                    console.log(res)
                    dataSrc = $('#tblTrainer').DataTable({
                        paging: true,
                        lengthChange: true,
                        searching: true,
                        ordering: true,
                        info: true,
                        autoWidth: true,
                        data: res,
                        columnDefs: [
                            {
                                targets: 4,
                                searchable: false,
                                orderable: false,
                                render: function (data, type, row) {
                                    if (type === 'display') {
                                        data = '<a href="' + data + ' " target="_blank"> ' + "Link hình" + '</a>';
                                    }
                                    return data;
                                }
                            }, {
                                targets: 6,
                                searchable: false,
                                orderable: false,
                                className: 'dt-body-center',
                                render: function (data, type, full, meta) {
                                    return '<input type="checkbox" class="editor-active">';
                                }
                            }
                        ],
                        columns: [
                            {data: "username"},
                            {data: "email"},
                            {data: "phone"},
                            {data: "gender"},
                            {data: "imgUrl"},
                            {data: "address"},
                            {data: "status"},
                            {data: "createdTime"},
                            {data: "updatedTime"},
                            {
                                data: "id",
                                searchable: false,
                                orderable: false,
                                render: function (data, type, row) {
                                    if (type === 'display') {
                                        data = '<button value="' + data + '" type="button" class="btn btn-block btn-info btn-edit">Cập nhật</button>';
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

                }
            })

            $('#tblTrainer').on('click', 'tbody .btn-edit', function (e) {
                var data_row = dataSrc.row($(this).parents('tr')).data();
                var status;
                //console.log(data_row);
                var $chkbox = $(this).closest('tr').find('input[type="checkbox"]');
                if ($chkbox.length) {
                    status = $chkbox.prop('checked');
                    if (status) {
                        data_row.status = "active";
                    } else {
                        data_row.status = "unactive";
                    }
                }
                console.log(data_row);
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
                                alert("Cập nhập thành công!")
                            } else {
                                alert("Cập nhập thất bại!")
                            }
                        } else {
                            console.log("update thật bại");
                        }
                    }
                })
            })

        } else {
            window.location.href = "403.html";
        }
    }
})