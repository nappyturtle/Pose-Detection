function retrieveFormInfo() {
    var data = JSON.parse(localStorage.getItem("staff"));
    var username1 = document.getElementById('pname_staff1');
    username1.innerHTML = data.username;
    var imageUser1 = document.getElementById('img_user1');
    imageUser1.src = data.imgUrl;

    var username2 = document.getElementById('pname_staff2');
    username2.innerHTML = data.username;
    var imageUser2 = document.getElementById('img_user2');
    imageUser2.src = data.imgUrl;

    var username3 = document.getElementById('pname_staff3');
    username3.innerHTML = data.username;
    var imageUser3 = document.getElementById('img_user3');
    imageUser3.src = data.imgUrl;
}

function getVideoListData() {
    var staff = JSON.parse(localStorage.getItem("staff"));
    var token = staff.token;
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var data = this.response;
            console.log('data = ' + data);
            var jsonResponse = JSON.parse(data);

            var table = document.createElement('table');
            table.setAttribute('class', 'table table-bordered table-striped dataTable no-footer');
            table.setAttribute('id', 'tblTrainer');
            table.setAttribute('style', 'text-align: center');

            // các key của json list object video response về
            var propertiesRes = ['id', 'title', 'thumnailUrl', 'contentUrl', 'numOfView', 'status', 'courseName', '']; // changed this
            // list tên các th
            var propertiesTemp = ['Stt', 'Tên video', 'Ảnh đại diện', 'Nội dung', 'Số lượt xem', 'Trạng thái', 'Tên khóa học', '']; // changed this
            var thead = document.createElement('thead');
            var tr = document.createElement('tr');
            tr.setAttribute('role', 'row');
            thead.appendChild(tr);
            for (var i = 0; i < propertiesTemp.length; i++) {
                var th = document.createElement('th');

                if (propertiesTemp[i] == "Stt"){
                    th.setAttribute('style', 'width: 30px; height: 50px;text-align: center;');
                } else {
                    th.setAttribute('style', 'width: 100px; height: 50px;text-align: center;');
                }

                th.appendChild(document.createTextNode(propertiesTemp[i]));
                tr.appendChild(th);
            }
            table.appendChild(tr);
            var tr, row;
            var tbody = document.createElement('tbody');
            console.log("jsonResponse", jsonResponse); // changed this
            for (var r = 0; r < jsonResponse.length; r++) { // changed this
                tr = document.createElement('tr');
                tr.setAttribute('role', 'row');
                if (r % 2 == 0) {
                    tr.setAttribute('class', 'odd');
                } else {
                    tr.setAttribute('class', 'even');
                }
                row = jsonResponse[r]; // changed this

                for (var i = 0; i < propertiesRes.length; i++) {
                    var td = document.createElement('td');
                    if (propertiesRes[i] == "") {
                        var button = document.createElement('button');
                        button.setAttribute('type', 'button');
                        button.setAttribute('class', 'btn btn-block btn-primary');
                        button.setAttribute('id', "btnEdit"+jsonResponse[r].id);
                        button.setAttribute('onclick', "editStatusVideo('myCheck" + jsonResponse[r].id + "')");
                        var buttonText = document.createTextNode("Cập nhập");
                        button.appendChild(buttonText);
                        td.appendChild(button);
                    } else {
                        if (row[propertiesRes[i]] == "active") {
                            var input = document.createElement('input');
                            input.setAttribute('type', 'checkbox');
                            input.setAttribute('id', 'myCheck' + jsonResponse[r].id);
                            input.setAttribute('value', 'active');
                            input.setAttribute('checked', 'true');

                            td.appendChild(input);
                        } else if (row[propertiesRes[i]] == "inactive") {
                            var input = document.createElement('input');
                            input.setAttribute('type', 'checkbox');
                            input.setAttribute('id', 'myCheck' + jsonResponse[r].id);
                            input.setAttribute('value', 'inactive');
                            input.setAttribute('unchecked', 'false');
                            td.appendChild(input);
                        } else if (propertiesRes[i] == "thumnailUrl" || propertiesRes[i] == "contentUrl") {
                            var link = document.createElement('a');
                            var linkText = document.createTextNode("Link");
                            link.setAttribute('href', row[propertiesRes[i]]);
                            link.appendChild(linkText);
                            td.appendChild(link);
                        } else {
                            td.appendChild(document.createTextNode(row[propertiesRes[i]]));
                        }
                    }
                    tr.appendChild(td);
                }
                tbody.appendChild(tr);
            }

            table.appendChild(tbody);

            document.getElementById('div_table_video_list').appendChild(table);
        }
    };
    xhttp.open("GET", "/video/getAllVideoByStaffOrAdmin", true);
    //xhttp.responseType = "json";
    xhttp.setRequestHeader("Authorization", token);
    xhttp.send();
}

function editStatusVideo(myCheck) {
    console.log(myCheck);
    // lấy giá trị id của checkbox
    // ví dụ myCheck123
    var checked = document.getElementById(myCheck).checked;
    var videoId = parseInt(myCheck.replace("myCheck",""),10); // replace myCheck để lấy 123 và ép kiểu thành int

    console.log("id = "+typeof videoId);

    var status = "";
    if (checked == true) {
        status = "active"
    } else {
        status = "inactive";
    }

    var data = JSON.parse(localStorage.getItem("staff"));
    var token = data.token;
    var r = confirm("Bạn có muốn thay đổi thông tin không?");
    if (r == true) {
        console.log("You pressed OK!");
        $.ajax({
            url: "/video/editVideoStatusByStaffOrAdmin",
            type: "PUT",
            data: jQuery.param({ id: videoId, status : status}),
            headers: {
                "content-type": "application/x-www-form-urlencoded; charset=UTF-8",
                "Authorization": token
            },
            success: function (res) {
                if (res != null) {
                    console.log(res);
                    if(res == true){
                        alert("Thay đổi thông tin thành công");
                        location.reload();
                    }else{
                        alert("Thay đổi thông tin thất bại");
                    }
                } else {
                    console.log("null");
                }
            }
        })
    } else {
        console.log("You pressed Cancel!");
    }

}

function loadData() {
    retrieveFormInfo();
    getVideoListData();
}
