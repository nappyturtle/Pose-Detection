var editor;

$(document).ready(function () {
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
            initTable();
        } else {
            window.location.href = "403.html";
        }
    } else {
        window.location.href = "403.html";
    }

    function initTable() {
        var dataSrc;
        $.ajax({
            url: "/suggestion/getAllSuggestionByStaffOrAdmin",
            type: "GET",
            headers: {
                "Authorization": currentStaff.token
            },
            success: function (res) {
                console.log(res)
                dataSrc = $('#tblSuggestion').DataTable({
                    paging: true,
                    lengthChange: true,
                    searching: true,
                    ordering: true,
                    info: true,
                    //autoWidth: true,
                    data: res

                    ,
                    columns: [

                        {data: "stt"},
                        {data: "videoname"},
                        {data: "accountname"},
                        {
                            data: "id",
                            searchable: false,
                            orderable: false,
                            render: function (data, type, row) {
                                if (type === 'display') {
                                    data = '<button type="button" class="btn btn-info btn-get-details" value="' + ("btnSuggestion") + data + '">Xem chi tiết</button>';
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
                    }
                });
                getSuggestion($('#tblSuggestion'), dataSrc);
            }
        })
    }

    function getSuggestion($table, dataSrc) {
        $table.on('click', 'tbody .btn-get-details', function (e) {
            var suggestionId = $(this).closest('tr').find('.btn-get-details').val().replace("btnSuggestion", "");
            window.location.href = "details.html?suggestionId=" + suggestionId;
        })
    }


})