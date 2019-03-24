// function retrieveFormInfo() {
//     var data = JSON.parse(localStorage.getItem("staff"));
//     var username1 = document.getElementById('pname_staff1');
//     username1.innerHTML = data.username;
//     var imageUser1 = document.getElementById('img_user1');
//     imageUser1.src = data.imgUrl;
//
//     var username2 = document.getElementById('pname_staff2');
//     username2.innerHTML = data.username;
//     var imageUser2 = document.getElementById('img_user2');
//     imageUser2.src = data.imgUrl;
//
//     var username3 = document.getElementById('pname_staff3');
//     username3.innerHTML = data.username;
//     var imageUser3 = document.getElementById('img_user3');
//     imageUser3.src = data.imgUrl;
// }
//
// function getSuggestionDetailListData() {
//     var staff = JSON.parse(localStorage.getItem("staff"));
//     var token = staff.token;
//     var xhttp = new XMLHttpRequest();
//     xhttp.onreadystatechange = function () {
//         if (this.readyState == 4 && this.status == 200) {
//             var data = this.response;
//             console.log('data = ' + data);
//             var jsonResponse = JSON.parse(data);
//
//             var table = document.createElement('table');
//             table.setAttribute('class', 'table table-bordered table-striped dataTable no-footer');
//             table.setAttribute('id', 'tblTrainer');
//
//             table.setAttribute('style', 'text-align: center');
//             // các key của json list object video response về
//             var propertiesRes = ['id', 'imgUrl', 'standardImgUrl', 'description', 'status', 'comment','']; // changed this
//             // list tên các th
//             var propertiesTemp = ['Stt', 'Hình trainer', 'Hình trainee', 'Mô tả', 'Trạng thái', 'Bình luận','']; // changed this
//
//             var thead = document.createElement('thead');
//             var tr = document.createElement('tr');
//             tr.setAttribute('role', 'row');
//             thead.appendChild(tr);
//             for (var i = 0; i < propertiesTemp.length; i++) {
//                 var th = document.createElement('th');
//
//                 if (propertiesTemp[i] == "Stt"){
//                     th.setAttribute('style', 'width: 30px; height: 50px;text-align: center;');
//                 } else {
//                     th.setAttribute('style', 'width: 100px; height: 50px;text-align: center;');
//                 }
//
//                 th.appendChild(document.createTextNode(propertiesTemp[i]));
//                 tr.appendChild(th);
//             }
//             table.appendChild(tr);
//             var tr, row;
//             var tbody = document.createElement('tbody');
//             console.log("jsonResponse", jsonResponse); // changed this
//             for (var r = 0; r < jsonResponse.length; r++) { // changed this
//                 tr = document.createElement('tr');
//                 tr.setAttribute('role', 'row');
//                 if (r % 2 == 0) {
//                     tr.setAttribute('class', 'odd');
//                 } else {
//                     tr.setAttribute('class', 'even');
//                 }
//                 row = jsonResponse[r]; // changed this
//
//                 for (var i = 0; i < propertiesRes.length; i++) {
//                     var td = document.createElement('td');
//                     if (propertiesRes[i] == "") {
//                         var button = document.createElement('button');
//                         button.setAttribute('type', 'button');
//                         button.setAttribute('class', 'btn btn-block btn-primary');
//                         button.setAttribute('id', "btnEdit"+jsonResponse[r].id);
//                         button.setAttribute('onclick', "editStatusSuggestionDetail('myCheck" + jsonResponse[r].id + "')");
//                         var buttonText = document.createTextNode("Cập nhập");
//                         button.appendChild(buttonText);
//                         td.appendChild(button);
//                     } else {
//                         if (row[propertiesRes[i]] == "active" || row[propertiesRes[i]] == "processing") {
//                             var input = document.createElement('input');
//                             input.setAttribute('type', 'checkbox');
//                             input.setAttribute('id', 'myCheck' + jsonResponse[r].id);
//                             input.setAttribute('value', 'active');
//                             input.setAttribute('checked', 'true');
//
//                             td.appendChild(input);
//                         } else if (row[propertiesRes[i]] == "inactive") {
//                             var input = document.createElement('input');
//                             input.setAttribute('type', 'checkbox');
//                             input.setAttribute('id', 'myCheck' + jsonResponse[r].id);
//                             input.setAttribute('value', 'inactive');
//                             input.setAttribute('unchecked', 'false');
//                             td.appendChild(input);
//                         } else if (propertiesRes[i] == "imgUrl" || propertiesRes[i] == "standardImgUrl") {
//                             var link = document.createElement('a');
//                             var linkText = document.createTextNode("Link");
//                             link.setAttribute('href', row[propertiesRes[i]]);
//                             link.appendChild(linkText);
//                             td.appendChild(link);
//                         } else {
//                             td.appendChild(document.createTextNode(row[propertiesRes[i]]));
//                         }
//                     }
//                     tr.appendChild(td);
//                 }
//                 tbody.appendChild(tr);
//             }
//
//             table.appendChild(tbody);
//
//             document.getElementById('div_table_video_list').appendChild(table);
//         }
//     };
//     xhttp.open("GET", "/suggestiondetail/getAllSuggestionDetailByStaffOrAdmin", true);
//     //xhttp.responseType = "json";
//     xhttp.setRequestHeader("Authorization", token);
//     xhttp.send();
// }
//
// function editStatusSuggestionDetail(myCheck) {
//     console.log(myCheck);
//     // lấy giá trị id của checkbox
//     // ví dụ myCheck123
//     var checked = document.getElementById(myCheck).checked;
//     var videoId = parseInt(myCheck.replace("myCheck",""),10);// replace myCheck để lấy 123 và ép kiểu thành int
//
//     console.log("id = "+typeof videoId);
//
//     var status = "";
//     if (checked == true) {
//         status = "active"
//     } else {
//         status = "inactive";
//     }
//
//     var data = JSON.parse(localStorage.getItem("staff"));
//     var token = data.token;
//     var r = confirm("Bạn có muốn thay đổi thông tin không?");
//     if (r == true) {
//         console.log("You pressed OK!");
//         $.ajax({
//             url: "/suggestiondetail/editStatusSuggestionDetailByStaffOrAdmin",
//             type: "PUT",
//             data: jQuery.param({ id: videoId, status : status}),
//             headers: {
//                 "content-type": "application/x-www-form-urlencoded; charset=UTF-8",
//                 "Authorization": token
//             },
//             success: function (res) {
//                 if (res != null) {
//                     console.log(res);
//                     if(res == true){
//                         alert("Thay đổi thông tin thành công");
//                         location.reload();
//                     }else{
//                         alert("Thay đổi thông tin thất bại");
//                     }
//                 } else {
//                     console.log("null");
//                 }
//             }
//         })
//     } else {
//         console.log("You pressed Cancel!");
//     }
//
// }
//
// function loadData() {
//     retrieveFormInfo();
//     getSuggestionDetailListData();
// }


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

            var dataSrc;
            $.ajax({
                url: "/suggestiondetail/getAllSuggestionDetailByStaffOrAdmin",
                type: "GET",
                headers: {
                    "Authorization": currentStaff.token
                },
                success: function (res) {
                    console.log(res)
                    dataSrc = $('#tblSuggestionDetail').DataTable({
                        paging: true,
                        lengthChange: true,
                        searching: true,
                        ordering: true,
                        info: true,
                        autoWidth: true,
                        data: res,
                        columnDefs: [
                            {
                                targets: 1,
                                searchable: false,
                                orderable: false,
                                render: function (data, type, row) {
                                    if (type === 'display') {
                                        data = '<a href="' + data + ' " target="_blank"> ' + "Link hình" + '</a>';
                                    }
                                    return data;
                                }
                            },
                            {
                                targets: 2,
                                searchable: false,
                                orderable: false,
                                render: function (data, type, row) {
                                    if (type === 'display') {
                                        data = '<a href="' + data + ' " target="_blank"> ' + "Link hình" + '</a>';
                                    }
                                    return data;
                                }
                            },
                            {
                                targets: 4,
                                searchable: false,
                                orderable: false,
                                className: 'dt-body-center',
                                render: function (data, type, full, meta) {
                                    return '<input type="checkbox" class="editor-active">';
                                }
                            }
                        ],
                        columns: [
                            {data: "id"},
                            {data: "imgUrl"},
                            {data: "standardImgUrl"},
                            {data: "description"},
                            {data: "status"},
                            {data: "comment"},
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

            $('#tblSuggestionDetail').on('click', 'tbody .btn-edit', function (e) {
                var data_row = dataSrc.row($(this).parents('tr')).data();
                var status;
                //console.log(data_row);
                var $chkbox = $(this).closest('tr').find('input[type="checkbox"]');
                if ($chkbox.length) {
                    status = $chkbox.prop('checked');
                    if (status) {
                        data_row.status = "active";
                    } else {
                        data_row.status = "inactive";
                    }
                }
                console.log(data_row);
                var r = confirm("Bạn có muốn thay đổi thông tin không?");
                if (r == true) {
                    console.log("You pressed OK!");
                    $.ajax({
                        url: "/suggestiondetail/editStatusSuggestionDetailByStaffOrAdmin",
                        type: "PUT",
                        data: JSON.stringify(data_row),
                        headers: {
                            "content-type": "application/json; charset=UTF-8",
                            "Authorization": currentStaff.token
                        },
                        dataType: "json",
                        success: function (res) {
                            if (res != null) {
                                if (res == true) {
                                    alert("Cập nhập thành công!");

                                } else {
                                    alert("Cập nhập thất bại!")
                                }
                            } else {
                                console.log("update thật bại");
                            }
                        }
                    })
                } else {
                    console.log("You pressed Hủy");
                }

            })

        } else {
            window.location.href = "403.html";
        }
    }
})