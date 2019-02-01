<%@page import="com.group9.pdst.utils.ConstantUtilities"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>

    <!-- Load TensorFlow.js -->
    <script src="https://cdn.jsdelivr.net/npm/@tensorflow/tfjs@0.11.7"></script>
    <!-- Load Posenet -->
    <script src="https://cdn.jsdelivr.net/npm/@tensorflow-models/posenet@0.1.2"></script>

    <script src="https://www.gstatic.com/firebasejs/5.7.2/firebase.js"></script>
    <script>
        // Initialize Firebase
        // TODO: Replace with your project's customized code snippet
        var config = {
            apiKey: "AIzaSyCBKsbivTw6LRS95Ozb-5S5ivLTBUY2jYg",
            authDomain: "mon-super-project-27e2b.firebaseapp.com",
            databaseURL: "https://mon-super-project-27e2b.firebaseio.com",
            projectId: "mon-super-project-27e2b",
            storageBucket: "gs://mon-super-project-27e2b.appspot.com",
            messagingSenderId: "1099210452571"
        };
        firebase.initializeApp(config);
    </script>

    <script language="JavaScript" type="text/javascript">
        var poses = new Map();
        function getPoses() {
            imgName = getParams("img") + ".png";
            simgName = getParams("simg") + ".png";
            getImageFromStorage(imgName, false);
            getImageFromStorage(simgName, true);
            var readyStateCheckInterval = setInterval(function () {
                if (poses.size === 2) {
                    clearInterval(readyStateCheckInterval);
                    sendKeypoints();
                }
            }, 10);
        }

        function getParams(key){
            var params={};
            location.search.replace(/[?&;]+([^=]+)=([^&;]*)/gi,function(s,key,value){params[key]=value})
            return key?params[key]:params;
        }

        function getImageFromStorage(imgName, standard) {
            var storage = firebase.storage();
            var storageRef = storage.ref();
            var imageElement = document.createElement("img");
            var div = document.createElement("div");
            document.getElementsByTagName('body')[0].appendChild(div);
            div.className = "imgDiv";
            imageElement.crossOrigin = "anonymous";
            imageElement.width = <%= ConstantUtilities.imgSize%>;
            imageElement.height = imageElement.width;
            imageElement.id = imgName;
            var divArr = document.getElementsByClassName("imgDiv");
            var position = divArr.length-1;
            storageRef.child(imgName).getDownloadURL().then(function (url) {
                imageElement.src = url;
                divArr[position].appendChild(imageElement);
                // document.getElementsByTagName('body')[0].appendChild(imageElement);
                var readyStateCheckInterval = setInterval(function () {
                    if (imageElement.complete === true) {
                        clearInterval(readyStateCheckInterval);
                        getPoseOfImage(imageElement, standard, position, url);
                    }
                }, 10);
            }).catch(function (error) {
                console.log("Firebase get image error: " + error.message);
            });
        }

        function getPoseOfImage(imageElement, standard, position, url) {
            var flipHorizontal = false;
            var outputStride = 8;
            var divArr = document.getElementsByClassName("imgDiv");
            posenet.load().then(function (net) {
                return net.estimateSinglePose(imageElement, 0.5, flipHorizontal, outputStride);
            }).then(function (pose) {

                count = 0;
                for(var i=0; i < pose.keypoints.length; i++) {
                    keypoint = pose.keypoints[i];
                    var pointDiv = document.createElement('div');
                    pointDiv.className = 'point';
                    pointDiv.style.left = keypoint.position.x + "px";
                    pointDiv.style.top = keypoint.position.y + "px";
                    pointDiv.innerHTML = count;
                    divArr[position].appendChild(pointDiv);
                    count++;
                }
                myPose = {keypoints: pose.keypoints, url: url};
                if(standard) {
                    poses.set("standard", myPose);
                }
                else {
                    poses.set("normal", myPose);
                }
            });
        }

        function sendKeypoints() {
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    console.log(this.responseText);
                }
            };
            xhttp.open("POST", "http://localhost:8080/matchPose", true);
            xhttp.setRequestHeader("Content-type", "application/json");
            var poseArr = [poses.get("standard"), poses.get("normal")]
            xhttp.send(JSON.stringify(poseArr));
        }
    </script>
    <style>
        .point {
            width: 5px;
            height: 5px;
            background-color: red;
            position: absolute;
            z-index: 2;
        }
        .imgDiv {
            float: left;
            position: relative;
        }
    </style>
</head>

<body onload="getPoses()">
</body>

</html>


